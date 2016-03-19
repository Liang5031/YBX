package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.BindingDeptListAdapter;
import com.ybx.guider.adapters.DeptListAdapter;
import com.ybx.guider.dialog.AddDeptDialog;
import com.ybx.guider.dialog.DeptItemDialog;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.BindingDeptItem;
import com.ybx.guider.responses.BindingDeptListResponse;
import com.ybx.guider.responses.DeptItem;
import com.ybx.guider.responses.DeptListResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindDeptActivity extends AppCompatActivity implements Response.Listener<DeptListResponse>, Response.ErrorListener,
        AdapterView.OnItemClickListener, AddDeptDialog.DeptItemDialogListener {
    private Spinner mSpinnerDeptType;
    private ArrayAdapter<String> mDeptTypeAdapter;
    XMLRequest<DeptListResponse> mRequest;
    XMLRequest<XMLResponse> mDeptAddRequest;
    String[] mDeptTypeList = {"旅行社", "部门"};
    ListView mListView;
    DeptListAdapter mAdapter;
    ProgressDialog mProgressDialog;
    ArrayList<DeptItem> mAllItems;
    EditText mKeyWord;
    int mLastItem;
    int mPageIndex;
    String mIndexCode = "";
    String mIndexName = "";
    String mCustomerType = "1";
    boolean isLoadFinished = false;

    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);

    public static boolean isContainsChinese(String str) {
        Matcher matcher = pat.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_dept);
        setTitle("添加部门");
        mAllItems = new ArrayList<DeptItem>();

        mListView = (ListView) findViewById(R.id.deptList);
//        mListView.setEmptyView(findViewById(R.id.empty));
        mAdapter = new DeptListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mLastItem == mAllItems.size()-1 && !isLoadFinished) {
                    Toast.makeText(BindDeptActivity.this, "Loading ...", Toast.LENGTH_LONG).show();
                    requestDeptList(mPageIndex);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastItem = mListView.getLastVisiblePosition();
            }
        });

        mKeyWord = (EditText) findViewById(R.id.keyword);

        mSpinnerDeptType = (Spinner) findViewById(R.id.spinnerType);
        mDeptTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mDeptTypeList);
        mDeptTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDeptType.setAdapter(mDeptTypeAdapter);
        mSpinnerDeptType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mCustomerType = "1";
                } else {
                    mCustomerType = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequest != null) {
            mRequest.cancel();
        }

        if (mDeptAddRequest != null) {
            mDeptAddRequest.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                Toast.makeText(this, "refresh pressed", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickQuery(View view) {
        if (inputCheck()) {
            mAllItems.clear();
            mPageIndex = 0;
            mProgressDialog = ProgressDialog.show(this, "正在加载数据", "请稍等...", true, false);
            requestDeptList(0);
        }

    }

    boolean inputCheck() {
        if (mKeyWord.getText().toString().isEmpty()) {
            Toast.makeText(this, "关键字不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        String keyword = mKeyWord.getText().toString();
        if (keyword.length() < 3) {
            Toast.makeText(this, "关键字长度不能小于3", Toast.LENGTH_LONG).show();
            return false;
        } else if (isContainsChinese(keyword)) {
            mIndexName = keyword;
            mIndexCode = "";
        } else {
            mIndexCode = keyword;
            mIndexName = "";
        }


        return true;
    }

    void requestDeptList(Integer pageIndex) {
        Param param = new Param(ParamUtils.PAGE_CUSTOMER_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setPageIndex(pageIndex.toString());
        if (!mIndexName.isEmpty()) {
            param.addParam(ParamUtils.KEY_CUSTOMER_NAME, mIndexName);
        }
        if (!mIndexCode.isEmpty()) {
            param.addParam(ParamUtils.KEY_INDEX_CODE, mIndexCode);
        }

        param.addParam(ParamUtils.KEY_CUSTOMER_TYPE, mCustomerType);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<DeptListResponse>(url, this, this, new DeptListResponse());
        mRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
        Toast.makeText(this, "查询部门失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(DeptListResponse response) {
        mProgressDialog.dismiss();
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (DeptItem item : response.mItems) {
                mAllItems.add(item);
            }

            mAdapter.update(mAllItems);
            mPageIndex = response.mPageIndex+1;

            if (1 == response.mIsLastPage) {
                isLoadFinished = true;
                if (mAllItems.size() == 0) {
                    Toast.makeText(this, "未查询到符合条件的旅行社或部门！", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(this, "查询部门失败！", Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DeptItem item = (DeptItem) mAdapter.getItem(position);
        BindingDeptItem bindItem = new BindingDeptItem();
        bindItem.customerid = item.customerid;
        bindItem.customername = item.customername;
        bindItem.customertype = item.type;

        if (item != null) {
            AddDeptDialog dlg = AddDeptDialog.newInstance(bindItem);
            dlg.show(getSupportFragmentManager(), "dept");
        }
    }

    @Override
    public void onDeptAdd(BindingDeptItem item) {
        mProgressDialog = ProgressDialog.show(this, "正在添加", "请稍等...", true, false);
        requestAddDept(item);
    }

    @Override
    public void onDeptCancel(BindingDeptItem item) {

    }

    void requestAddDept(final BindingDeptItem item) {
        Param param = new Param(ParamUtils.PAGE_DEPT_ADD);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_CUSTOMER_TYPE, item.customertype);
        param.addParam(ParamUtils.KEY_CUSTOMER_ID, item.customerid);
        param.addParam(ParamUtils.KEY_YYXCXK, item.YYXCXK);
        param.addParam(ParamUtils.KEY_WPTDXK, item.WPTDXK);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> resListener = new Response.Listener<XMLResponse>() {

            @Override
            public void onResponse(XMLResponse response) {
                mProgressDialog.dismiss();
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(BindDeptActivity.this, "添加部门成功！", Toast.LENGTH_LONG).show();
//                    mAllItems.remove(item);
                    remove(item);
                    mAdapter.update(mAllItems);
                } else {
                    Toast.makeText(BindDeptActivity.this, "添加部门失败！", Toast.LENGTH_LONG).show();
                    if (URLUtils.isDebug) {
                        Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                        Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(BindDeptActivity.this, "添加部门失败！", Toast.LENGTH_LONG).show();

                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }
            }
        };

        String url = URLUtils.generateURL(param);
        mDeptAddRequest = new XMLRequest<XMLResponse>(url, resListener, errorListener, new XMLResponse());
        mDeptAddRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mDeptAddRequest);
    }

    void remove(BindingDeptItem item){
        Iterator iter = mAllItems.iterator();
        while (iter.hasNext()) {
            DeptItem i = (DeptItem)iter.next();
            if (i.customerid.equals(item.customerid)) {
                iter.remove();
            }
        }
    }
}
