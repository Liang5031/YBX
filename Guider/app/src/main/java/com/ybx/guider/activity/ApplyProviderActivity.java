package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.ProviderListAdapter;
import com.ybx.guider.dialog.AddDeptDialog;
import com.ybx.guider.dialog.ApplyProviderDialog;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.BindingDeptItem;
import com.ybx.guider.responses.DeptItem;
import com.ybx.guider.responses.ProviderItem;
import com.ybx.guider.responses.ProviderListResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplyProviderActivity extends AppCompatActivity implements Response.Listener<ProviderListResponse>, Response.ErrorListener,
        AdapterView.OnItemClickListener, ApplyProviderDialog.ApplyProviderDialogListener {
    private Spinner mSpinnerProviderType;
    private ArrayAdapter<String> mProviderTypeAdapter;
    XMLRequest<ProviderListResponse> mRequest;
    XMLRequest<XMLResponse> mApplyProviderRequest;

    String[] mProviderTypeList = {"吃", "住", "行", "游", "购", "娱"};
    ListView mListView;
    ProviderListAdapter mAdapter;
    ProgressDialog mProgressDialog;
    ArrayList<ProviderItem> mAllItems;
    EditText mKeyWord;
    int mLastPosition;
    int mPageIndex;
    String mIndexCode = "";
    String mIndexName = "";
    String mProviderType = "1";
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
        setContentView(R.layout.activity_apply_provider);
        setTitle("供应商预约申请");
        mAllItems = new ArrayList<ProviderItem>();

        mListView = (ListView) findViewById(R.id.providerList);
        mAdapter = new ProviderListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mLastPosition == mAllItems.size() - 1 && !isLoadFinished) {
                    Toast.makeText(ApplyProviderActivity.this, "Loading ...", Toast.LENGTH_LONG).show();
                    requestProviderList(mPageIndex);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastPosition = mListView.getLastVisiblePosition();
            }
        });

        mKeyWord = (EditText) findViewById(R.id.keyword);

        mSpinnerProviderType = (Spinner) findViewById(R.id.spinnerType);
        mProviderTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mProviderTypeList);
        mProviderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerProviderType.setAdapter(mProviderTypeAdapter);
        mSpinnerProviderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        mProviderType = "1?????";
                        break;
                    case 1:
                        mProviderType = "?1????";
                        break;
                    case 2:
                        mProviderType = "??1???";
                        break;
                    case 3:
                        mProviderType = "???1??";
                        break;
                    case 4:
                        mProviderType = "????1?";
                        break;
                    case 5:
                        mProviderType = "?????1";
                        break;
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

        if (mApplyProviderRequest != null) {
            mApplyProviderRequest.cancel();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.refresh_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_refresh:
//                Toast.makeText(this, "refresh pressed", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void onClickQuery(View view) {
        if (inputCheck()) {
            mAllItems.clear();
            mPageIndex = 0;
            isLoadFinished = false;
            mProgressDialog = ProgressDialog.show(this, "正在加载数据", "请稍等...", true, false);
            requestProviderList(0);
        }

    }

    boolean inputCheck() {
        if (mKeyWord.getText().toString().isEmpty()) {
            return true;
        }

        String keyword = mKeyWord.getText().toString();
        if (keyword.length() < 2) {
            Toast.makeText(this, "关键字长度不能小于2", Toast.LENGTH_LONG).show();
            return false;
        }

        if (isContainsChinese(keyword)) {
            mIndexName = keyword;
            mIndexCode = "";
        } else {
            mIndexCode = keyword;
            mIndexName = "";
        }

        return true;
    }

    void requestProviderList(Integer pageIndex) {
        Param param = new Param(ParamUtils.PAGE_PROVIDER_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_PROVIDER_TYPE,mProviderType);

        if (!mIndexName.isEmpty()) {
            param.addParam(ParamUtils.KEY_PROVIDER_NAME, mIndexName);
        }else if (!mIndexCode.isEmpty()) {
            param.addParam(ParamUtils.KEY_INDEX_CODE, mIndexCode);
        }
        param.setPageIndex(pageIndex.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<ProviderListResponse>(url, this, this, new ProviderListResponse());
        mRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
        Toast.makeText(this, "查询供应商信息失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(ProviderListResponse response) {
        mProgressDialog.dismiss();
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (ProviderItem item : response.mItems) {
                mAllItems.add(item);
            }

            mAdapter.update(mAllItems);
            mPageIndex = response.mPageIndex + 1;

            if (1 == response.mIsLastPage) {
                isLoadFinished = true;
                if (mAllItems.size() == 0) {
                    Toast.makeText(this, "未查询到符合条件的供应商！", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(this, response.mReturnMSG, Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProviderItem item = (ProviderItem) mAdapter.getItem(position);
        if (item != null && (item.appointmentable.equals("1") || item.appointmentable.equals("2"))) {
            ApplyProviderDialog dlg = ApplyProviderDialog.newInstance(item);
            dlg.show(getSupportFragmentManager(), "provider");
        }
    }

     void requestApplyProvider(final ProviderItem item) {
        Param param = new Param(ParamUtils.PAGE_APPOINTMENT_PERMISSION_REQUEST);
         param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_PROVIDER_ID, item.providerid);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> resListener = new Response.Listener<XMLResponse>() {

            @Override
            public void onResponse(XMLResponse response) {
                mProgressDialog.dismiss();
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(ApplyProviderActivity.this, "已申请开通供应商预约！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ApplyProviderActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
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
                Toast.makeText(ApplyProviderActivity.this, "申请失败！", Toast.LENGTH_LONG).show();

                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }
            }
        };

        String url = URLUtils.generateURL(param);
        mApplyProviderRequest = new XMLRequest<XMLResponse>(url, resListener, errorListener, new XMLResponse());
        mApplyProviderRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mApplyProviderRequest);
    }

    void remove(ProviderItem item) {
        Iterator iter = mAllItems.iterator();
        while (iter.hasNext()) {
            ProviderItem i = (ProviderItem) iter.next();
            if (i.providerid.equals(item.providerid)) {
                iter.remove();
            }
        }
    }

    @Override
    public void onApply(ProviderItem item) {
        requestApplyProvider(item);
    }
}
