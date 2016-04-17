package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.RelatedProviderAdapter;
import com.ybx.guider.dialog.ProviderSyncDialog;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.RelatedProviderItem;
import com.ybx.guider.responses.RelatedProviderListResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

public class RelatedProviderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Response.Listener<RelatedProviderListResponse>,
        Response.ErrorListener, ProviderSyncDialog.ProviderSyncDialogListener {

    ListView mListView;
    RelatedProviderAdapter mAdapter;
    XMLRequest<RelatedProviderListResponse> mRequest;
    XMLRequest<XMLResponse> mSyncRequest;
    private ProgressDialog mProgressDialog;
    ArrayList<RelatedProviderItem> mAllItems;
    int mLastPosition;
    int mPageIndex;
    boolean isLoadFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_provider);

        this.setTitle("供应商列表");
        mAllItems = new ArrayList<RelatedProviderItem>();
        mListView = (ListView) findViewById(R.id.provider_list);
        mAdapter = new RelatedProviderAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mLastPosition == mAllItems.size() - 1 && !isLoadFinished) {
                    Toast.makeText(RelatedProviderActivity.this, "Loading ...", Toast.LENGTH_LONG).show();
                    requestProviderList(mPageIndex,false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastPosition = mListView.getLastVisiblePosition();
            }
        });


//        mAllItems.clear();
//        mPageIndex = 0;
//        isLoadFinished = false;
//        mProgressDialog = ProgressDialog.show(this, "正在查询供应商信息", "请稍等...", true, false);
//        requestProviderList(0, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAllItems.clear();
        mPageIndex = 0;
        isLoadFinished = false;
        mProgressDialog = ProgressDialog.show(this, "正在查询供应商信息", "请稍等...", true, false);
        requestProviderList(0, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRequest!=null){
            mRequest.cancel();
        }

        if(mSyncRequest!=null){
            mSyncRequest.cancel();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RelatedProviderItem item = (RelatedProviderItem) mAdapter.getItem(position);
//        if (item != null) {
            ProviderSyncDialog dlg = ProviderSyncDialog.newInstance(item);
            dlg.show(getSupportFragmentManager(), "dept");
//        }
    }

    void requestProviderList(Integer pageIndex, boolean isRefresh){
        Param param = new Param(ParamUtils.PAGE_PROVIDER_RELATED_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_PAGE_INDEX, pageIndex.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<RelatedProviderListResponse>(url, this, this, new RelatedProviderListResponse());
        mRequest.setShouldCache(false);
        if (isRefresh) {
            /* remove cache first */
            VolleyRequestQueue.getInstance(this).remove(url);
        }
        VolleyRequestQueue.getInstance(this).add(mRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        Toast.makeText(this, "查询供应商失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(RelatedProviderListResponse response) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (RelatedProviderItem item : response.mItems) {
                mAllItems.add(item);
            }
            mAdapter.update(mAllItems);
            mPageIndex = response.mPageIndex + 1;

            if (1 == response.mIsLastPage) {
                isLoadFinished = true;
                if (mAllItems.size() == 0) {
                    Toast.makeText(this, "未查询到供应商信息！", Toast.LENGTH_LONG).show();
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
    public void onSync(RelatedProviderItem item) {
        requestSyncProviderInfo(item);
    }

    void requestSyncProviderInfo(RelatedProviderItem item){
        if(item==null){
            return ;
        }

        Param param = new Param(ParamUtils.PAGE_APPOINTMENT_PERMISSION_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_PROVIDER_ID, item.providerid);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> responseListener = new Response.Listener<XMLResponse>() {
            @Override
            public void onResponse(XMLResponse response) {
                mProgressDialog.dismiss();
                if (response.mReturnCode.equalsIgnoreCase(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(RelatedProviderActivity.this, "同步成功！", Toast.LENGTH_LONG).show();
                    mAllItems.clear();
                    requestProviderList(0, true);
                } else {
                    Toast.makeText(RelatedProviderActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(RelatedProviderActivity.this, "同步供应商信息失败！", Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mSyncRequest = new XMLRequest<XMLResponse>(url, responseListener, errorListener, new XMLResponse());
        mSyncRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mSyncRequest);
    }

    public void onClickAdd(View view){
        Intent intent = new Intent(this, ApplyProviderActivity.class);
        startActivity(intent);
    }

}
