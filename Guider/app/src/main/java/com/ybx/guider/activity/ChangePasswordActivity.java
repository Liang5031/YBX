package com.ybx.guider.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResetPasswordResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

public class ChangePasswordActivity extends AppCompatActivity implements Response.Listener<XMLResponse>, Response.ErrorListener{

    XMLRequest<XMLResponse> mChangePWDRequest;
    EditText mGuiderNumber;
    EditText mOldPWD;
    EditText mNewPWD;
    EditText mConfirmPWD;
    CheckBox mRememberPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.setTitle("修改密码");
        mRememberPWD = (CheckBox)findViewById(R.id.checkboxRemember);
        mGuiderNumber = (EditText)findViewById(R.id.guiderNumber);
        mOldPWD = (EditText)findViewById(R.id.oldPassword);
        mNewPWD = (EditText)findViewById(R.id.newPassword);
        mConfirmPWD = (EditText)findViewById(R.id.confirmPassword);

        mGuiderNumber.setText(PreferencesUtils.getGuiderNumber(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChangePWDRequest!=null){
            mChangePWDRequest.cancel();
        }
    }

    public void onClickChangePassword(View view){
        if(inputCheck()){
            requestChangePWD();
        }
    }

    boolean inputCheck(){
        if(mGuiderNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "导游证号不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mOldPWD.getText().toString().isEmpty()){
            Toast.makeText(this, "旧密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mNewPWD.getText().toString().isEmpty()){
            Toast.makeText(this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mConfirmPWD.getText().toString().isEmpty()){
            Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!mNewPWD.getText().toString().equals(mConfirmPWD.getText().toString())){
            Toast.makeText(this, "确认密码和新密码不一致！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void requestChangePWD(){
        Param param = new Param(ParamUtils.PAGE_GUIDER_CHANGE_PASSWORD);
        param.setUser(mGuiderNumber.getText().toString());
        param.setNewPassword(EncryptUtils.md5(mNewPWD.getText().toString()).toUpperCase());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, mNewPWD.getText().toString());
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mChangePWDRequest = new XMLRequest<XMLResponse>(url, this, this, new XMLResponse());
        mChangePWDRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mChangePWDRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "修改密码失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(XMLResponse response) {
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            Toast.makeText(this, "修改密码成功！", Toast.LENGTH_LONG).show();
            if(mRememberPWD.isChecked()){
                PreferencesUtils.setGuiderNumber(this,mGuiderNumber.getText().toString());
                PreferencesUtils.setPassword(this, mNewPWD.getText().toString());
            }
        } else {
            Toast.makeText(this, "修改密码失败！", Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }
}
