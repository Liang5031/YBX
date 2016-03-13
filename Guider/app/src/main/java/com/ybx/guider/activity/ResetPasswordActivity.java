package com.ybx.guider.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.GetVerifyCodeResponse;
import com.ybx.guider.responses.ResetPasswordResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;


public class ResetPasswordActivity extends AppCompatActivity implements Response.Listener<ResetPasswordResponse>, Response.ErrorListener {
    private EditText mGuiderNumber;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private EditText mVerifyCode;
    private CheckBox mRememberPWD;
    XMLRequest<GetVerifyCodeResponse> mGetVerifyCodeRequest;
    XMLRequest<ResetPasswordResponse> mResetPWDRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passward);
        this.setTitle(R.string.reset_password);

        mRememberPWD = (CheckBox)findViewById(R.id.remember_password);
        mVerifyCode = (EditText)findViewById(R.id.verifyCode);
        mNewPassword = (EditText)findViewById(R.id.newPassword);
        mConfirmPassword = (EditText)findViewById(R.id.confirmPassword);
        mGuiderNumber = (EditText)findViewById(R.id.guiderNumber);
        mGuiderNumber.setText(PreferencesUtils.getGuiderNumber(this));
    }

    public void onClickGetVerifyCode(View view) {
        if(mGuiderNumber.getText().toString().isEmpty()){
            Toast.makeText(this,"导游证号不能为空！", Toast.LENGTH_LONG).show();
        }

        Param param = new Param(ParamUtils.PAGE_GET_CHECK_CODE);
        param.setGuiderNumber(mGuiderNumber.getText().toString());
        Response.Listener<GetVerifyCodeResponse> listener = new Response.Listener<GetVerifyCodeResponse>(){
            @Override
            public void onResponse(GetVerifyCodeResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(ResetPasswordActivity.this, "获取验证码成功!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "获取验证码失败!", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResetPasswordActivity.this,"获取验证码失败!",Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mGetVerifyCodeRequest = new XMLRequest<GetVerifyCodeResponse>(url,  listener, errorListener, new GetVerifyCodeResponse());
        mGetVerifyCodeRequest.setShouldCache(false);
        VolleyRequestQueue.getInstance(this).add(mGetVerifyCodeRequest);
    }

    private boolean inputCheck(){
        if (mGuiderNumber == null || mGuiderNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "导游证号不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mVerifyCode == null || mVerifyCode.getText().toString().isEmpty()) {
            Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mNewPassword == null || mNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "新密码不能为空！", Toast.LENGTH_LONG).show();
            return false;
        } else if (mNewPassword.getText().toString().length() < Utils.MIN_LENGTH_OF_PASSWORD) {
            Toast.makeText(this, "新密码长度不能小于6！", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utils.checkPassword(mNewPassword.getText().toString())) {
            Toast.makeText(this, "新密码必须同时包含字母和数字！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mConfirmPassword == null || mConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    public void onClickResetPassword(View view) {
        if(inputCheck()) {
            Param param = new Param(ParamUtils.PAGE_GUIDER_RESET_PASSWORD);
            param.setUser(mGuiderNumber.getText().toString());
            param.setVerifyCode(mVerifyCode.getText().toString());
//            param.setNewPassword(EncryptUtils.md5(mNewPassword.getText().toString()));
            param.setNewPassword(EncryptUtils.md5(mNewPassword.getText().toString()).toUpperCase());

            String url = URLUtils.generateURL(param);
            mResetPWDRequest = new XMLRequest<ResetPasswordResponse>(url, this, this, new ResetPasswordResponse());
            mResetPWDRequest.setShouldCache(false);
            VolleyRequestQueue.getInstance(this).add(mResetPWDRequest);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mResetPWDRequest!=null){
            mResetPWDRequest.cancel();
        }

        if(mGetVerifyCodeRequest!=null){
            mGetVerifyCodeRequest.cancel();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(ResetPasswordResponse response) {
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            Toast.makeText(this, "重置密码成功！", Toast.LENGTH_LONG).show();
            if(mRememberPWD.isChecked()){
                PreferencesUtils.setGuiderNumber(this,mGuiderNumber.getText().toString());
                PreferencesUtils.setPassword(this, mNewPassword.getText().toString());
            }
        } else {
            Toast.makeText(this, response.mReturnMSG, Toast.LENGTH_LONG).show();
        }
    }
}
