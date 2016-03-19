package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.BindingDeptListAdapter;
import com.ybx.guider.dialog.DeptItemDialog;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.BindingDeptItem;
import com.ybx.guider.responses.BindingDeptListResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

public class DeptManageActivity extends AppCompatActivity implements Response.Listener<BindingDeptListResponse>, Response.ErrorListener,
        AdapterView.OnItemClickListener, DeptItemDialog.DeptItemDialogListener {

    ListView mListView;
    BindingDeptListAdapter mAdapter;
    XMLRequest<BindingDeptListResponse> mDeptListRequest;
    XMLRequest<XMLResponse> mDeptChangeRequest;
    XMLRequest<XMLResponse> mDeptDeleteRequest;

    ArrayList<BindingDeptItem> mAllItems;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_manage);
        this.setTitle("旅行社/部门列表");

        mListView = (ListView) findViewById(R.id.dept_list);
        mAdapter = new BindingDeptListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mListView.setEmptyView(findViewById(R.id.empty));

        mAllItems = new ArrayList<BindingDeptItem>();
        mAllItems.clear();
        mProgressDialog = ProgressDialog.show(this, "正在查询部门数据", "请稍等...", true, false);
        requestBindingDeptList(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDeptListRequest != null){
            mDeptListRequest.cancel();
        }

        if(mDeptChangeRequest != null){
            mDeptChangeRequest.cancel();
        }

        if(mDeptDeleteRequest != null){
            mDeptDeleteRequest.cancel();
        }

    }

    void requestBindingDeptList(Integer pageIndex) {
        Param param = new Param(ParamUtils.PAGE_DEPT_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setPageIndex(pageIndex.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mDeptListRequest = new XMLRequest<BindingDeptListResponse>(url, this, this, new BindingDeptListResponse());
        mDeptListRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mDeptListRequest);
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
    public void onResponse(BindingDeptListResponse response) {
        mProgressDialog.dismiss();
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (BindingDeptItem item : response.mItems) {
                mAllItems.add(item);
            }

            if (1 == response.mIsLastPage) {
                mAdapter.update(mAllItems);
            } else {
                requestBindingDeptList(response.mPageIndex + 1);
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
        BindingDeptItem item = (BindingDeptItem) mAdapter.getItem(position);
        if (item != null) {
            DeptItemDialog dlg = DeptItemDialog.newInstance(item);
            dlg.show(getSupportFragmentManager(), "dept");
        }
    }

    @Override
    public void onDeptChange(BindingDeptItem item) {
        mProgressDialog = ProgressDialog.show(this, "正在更新数据", "请稍等...", true, false);
        requestChangeDept(item);
    }

    @Override
    public void onDeptCancel(BindingDeptItem item) {
        mProgressDialog = ProgressDialog.show(this, "正在更新数据", "请稍等...", true, false);
        requestDeleteDept(item);
    }

    void requestChangeDept(BindingDeptItem item) {
        Param param = new Param(ParamUtils.PAGE_DEPT_ADD);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_CUSTOMER_TYPE, item.customertype);
        param.addParam(ParamUtils.KEY_CUSTOMER_ID, item.customerid);
        param.addParam(ParamUtils.KEY_YYXCXK, item.YYXCXK);
        param.addParam(ParamUtils.KEY_WPTDXK, item.WPTDXK);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> resListener = new Response.Listener<XMLResponse>(){

            @Override
            public void onResponse(XMLResponse response) {
                mProgressDialog.dismiss();
                if(response.mReturnCode.equals(ResponseUtils.RESULT_OK)){
                    mAllItems.clear();
                    requestBindingDeptList(0);
                }else {
                    Toast.makeText(DeptManageActivity.this, "修改部门信息失败！", Toast.LENGTH_LONG).show();
                    if (URLUtils.isDebug) {
                        Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                        Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(DeptManageActivity.this, "修改部门信息失败！", Toast.LENGTH_LONG).show();

                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }
            }
        };

        String url = URLUtils.generateURL(param);
        mDeptChangeRequest = new XMLRequest<XMLResponse>(url, resListener, errorListener, new XMLResponse());
        mDeptChangeRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mDeptChangeRequest);
    }

    void requestDeleteDept(BindingDeptItem item) {
        Param param = new Param(ParamUtils.PAGE_DEPT_DELETE);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_CUSTOMER_TYPE, item.customertype);
        param.addParam(ParamUtils.KEY_CUSTOMER_ID, item.customerid);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> resListener = new Response.Listener<XMLResponse>(){

            @Override
            public void onResponse(XMLResponse response) {
                mProgressDialog.dismiss();
                if(response.mReturnCode.equals(ResponseUtils.RESULT_OK)){
                    mAllItems.clear();
                    requestBindingDeptList(0);
                } else {
                    Toast.makeText(DeptManageActivity.this, "取消部门关联失败！", Toast.LENGTH_LONG).show();
                    if (URLUtils.isDebug) {
                        Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                        Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(DeptManageActivity.this, "取消部门关联失败！", Toast.LENGTH_LONG).show();

                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }
            }
        };

        String url = URLUtils.generateURL(param);
        mDeptDeleteRequest = new XMLRequest<XMLResponse>(url, resListener, errorListener, new XMLResponse());
        mDeptDeleteRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mDeptDeleteRequest);
    }

    public void onClickAdd(View view){
        Intent intent = new Intent(this, BindDeptActivity.class);
        startActivity(intent);
//        this.finish();
    }
}
