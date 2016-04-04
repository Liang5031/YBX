package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.GuiderInfoResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

public class ChangeUserInfoActivity extends AppCompatActivity implements Response.Listener<GuiderInfoResponse>, Response.ErrorListener {

    EditText mGuiderNumber;
    EditText mUserName;
    EditText mUserMobile;
    EditText mFirstLanguage;
    XMLRequest<GuiderInfoResponse> mQueryGuiderInfo;
    XMLRequest<XMLResponse> mUpdateRequest;

    GuiderInfoResponse mGuiderInfo;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        setTitle("更新个人信息");

        mGuiderNumber = (EditText)findViewById(R.id.guiderNumber);
        mUserName = (EditText)findViewById(R.id.userName);
        mUserMobile = (EditText)findViewById(R.id.userMobile);
        mFirstLanguage = (EditText)findViewById(R.id.firstLanguage);
        mGuiderNumber.setText(PreferencesUtils.getGuiderNumber(this));
        mProgressDialog = ProgressDialog.show(this, "正在查询用户信息", "请稍等...", true, false);
        reqeustQuery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mQueryGuiderInfo!=null){
            mQueryGuiderInfo.cancel();
        }

        if(mUpdateRequest!=null){
            mUpdateRequest.cancel();
        }
    }

    boolean inputCheck(){

        if(mUserName.getText().toString().isEmpty()){
            Toast.makeText(this,"姓名不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if(mUserMobile.getText().toString().isEmpty()){
            Toast.makeText(this,"手机号不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if(mFirstLanguage.getText().toString().isEmpty()){
            Toast.makeText(this,"主要语种不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void onClickUpdate(View view) {
        if(inputCheck()) {
            mProgressDialog = ProgressDialog.show(this, "正在更新用户信息", "请稍等...", true, false);
            reqeustUpdate();
        }
    }

    void reqeustUpdate(){
        Param param = new Param(ParamUtils.PAGE_GUIDER_UPDATE_INFO);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.addParam(ParamUtils.KEY_GUIDER_NAME, mUserName.getText().toString());
        param.addParam(ParamUtils.KEY_PHONE_NUMBER, mUserMobile.getText().toString());
        param.addParam(ParamUtils.KEY_FIRST_LANGUAGE, mFirstLanguage.getText().toString());
        param.addParam(ParamUtils.KEY_PHOTO, PreferencesUtils.getGuiderNumber(this) + ".jpg" );

        String identidy = ((EditText)findViewById(R.id.userIdentity)).getText().toString();
        if(!identidy.isEmpty()){
            param.addParam(ParamUtils.KEY_GUIDER_IDENTITY_NUMBER, identidy);
        }

        String lang2 =  ((EditText)findViewById(R.id.secondLanguage)).getText().toString();
        if(!lang2.isEmpty()){
            param.addParam(ParamUtils.KEY_SECOND_LANGUAGE, lang2);
        }

        String addr1 = ((EditText)findViewById(R.id.userAddress1)).getText().toString();
        if(!addr1.isEmpty()){
            param.addParam(ParamUtils.KEY_ADDRESS_1, addr1);
        }

        String addrZone1 = ((EditText)findViewById(R.id.addrZone1)).getText().toString();
        if(!addrZone1.isEmpty()){
            param.addParam(ParamUtils.KEY_ADDRESS_ZONE_1, addrZone1);
        }

        String addr2 = ((EditText)findViewById(R.id.userAddress2)).getText().toString();
        if(!addr2.isEmpty()){
            param.addParam(ParamUtils.KEY_ADDRESS_2, addr2);
        }

        String addrZone2 = ((EditText)findViewById(R.id.addrZone2)).getText().toString();
        if(!addrZone2.isEmpty()){
            param.addParam(ParamUtils.KEY_ADDRESS_ZONE_2, addrZone2);
        }

        String education = ((EditText)findViewById(R.id.education)).getText().toString();
        if(!education.isEmpty()){
            param.addParam(ParamUtils.KEY_EDUCATION, education);
        }

        String graduation = ((EditText)findViewById(R.id.graduation)).getText().toString();
        if(!graduation.isEmpty()){
            param.addParam(ParamUtils.KEY_GRADUATION,graduation);
        }

        String phone = ((EditText)findViewById(R.id.userPhone)).getText().toString();
        if(!phone.isEmpty()){
            param.addParam(ParamUtils.KEY_PHONE, phone);
        }

        String qq = ((EditText)findViewById(R.id.userQQ)).getText().toString();
        if(!qq.isEmpty()){
            param.addParam(ParamUtils.KEY_QQ, qq);
        }

        String wechat = ((EditText)findViewById(R.id.userWeChat)).getText().toString();
        if(!wechat.isEmpty()){
            param.addParam(ParamUtils.KEY_WECHAT, wechat);
        }

        Response.Listener<XMLResponse> responseListener = new Response.Listener<XMLResponse>() {

            @Override
            public void onResponse(XMLResponse response) {
                mProgressDialog.dismiss();
                if (response.mReturnCode.equalsIgnoreCase(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(ChangeUserInfoActivity.this, "更新成功！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChangeUserInfoActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
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
                Toast.makeText(ChangeUserInfoActivity.this, "更新失败！", Toast.LENGTH_LONG).show();
                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }

            }
        };

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mUpdateRequest = new XMLRequest<XMLResponse>(url, responseListener, errorListener, new XMLResponse());
        mUpdateRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mUpdateRequest);
    }

    void reqeustQuery(){
        Param param = new Param(ParamUtils.PAGE_GUIDER_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this));

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mQueryGuiderInfo = new XMLRequest<GuiderInfoResponse>(url, this, this, new GuiderInfoResponse());
        mQueryGuiderInfo.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mQueryGuiderInfo);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        Toast.makeText(this, "查询用户信息失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(GuiderInfoResponse response) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            updateUI(response);
        } else {
            Toast.makeText(this, response.mReturnMSG, Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }

    void updateUI(GuiderInfoResponse response){
        ((EditText)findViewById(R.id.userName)).setText(response.Name);
        ((EditText)findViewById(R.id.userIdentity)).setText(response.IdentityNumber);
        ((EditText)findViewById(R.id.userMobile)).setText(response.Mobile);
        ((EditText)findViewById(R.id.firstLanguage)).setText(response.Language1);
        ((EditText)findViewById(R.id.secondLanguage)).setText(response.Language2);
        ((EditText)findViewById(R.id.userAddress1)).setText(response.Address1);
        ((EditText)findViewById(R.id.addrZone1)).setText(response.AddZone1);
        ((EditText)findViewById(R.id.userAddress2)).setText(response.Address2);
        ((EditText)findViewById(R.id.addrZone2)).setText(response.AddZone2);
        ((EditText)findViewById(R.id.education)).setText(response.Education);
        ((EditText)findViewById(R.id.graduation)).setText(response.Graducation);
        ((EditText)findViewById(R.id.userPhone)).setText(response.Phone);
        ((EditText)findViewById(R.id.userQQ)).setText(response.QQ);
        ((EditText)findViewById(R.id.userWeChat)).setText(response.Wechart);
    }
}
