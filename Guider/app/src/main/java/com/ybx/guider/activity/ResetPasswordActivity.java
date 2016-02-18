package com.ybx.guider.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;
import com.ybx.guider.parameters.GetVerifyCodeParam;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.parameters.ResetPasswordParam;
import com.ybx.guider.requests.GetVerifyCodeRequest;
import com.ybx.guider.requests.ResetPasswordRequest;
import com.ybx.guider.responses.GetVerifyCodeResponse;
import com.ybx.guider.responses.ResetPasswordResponse;


public class ResetPasswordActivity extends AppCompatActivity implements Response.Listener<ResetPasswordResponse>, Response.ErrorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passward);
        this.setTitle(R.string.reset_password);

        EditText et = (EditText)findViewById(R.id.guiderNumber);
        et.setText(PreferencesUtils.getGuiderNumber(this));
        /* Enable up button */
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void onClickResetPassword(View view) {
        ResetPasswordParam param = new ResetPasswordParam();
        param.setUser("user");
        param.setNewPassword("123456");
        ResetPasswordRequest request = new ResetPasswordRequest(URLUtils.generateURL(ParamUtils.PAGE_RESET_PASSWORD, param), this, this);
        VolleyRequestQueue.getInstance(this).add(request);
    }

    public void onClickGetVerifyCode(View view) {
        GetVerifyCodeParam param = new GetVerifyCodeParam();
        param.setPhoneNumber("123");

        Response.Listener<GetVerifyCodeResponse> listener = new Response.Listener<GetVerifyCodeResponse>(){
            @Override
            public void onResponse(GetVerifyCodeResponse response) {
                if (response.mReturnCode.equals(GetVerifyCodeResponse.RESULT_OK)) {
                    Toast.makeText(ResetPasswordActivity.this, "获取验证码成功!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResetPasswordActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        };

        GetVerifyCodeRequest request = new GetVerifyCodeRequest(URLUtils.generateURL(ParamUtils.PAGE_GET_VERIFY_CODE, param), listener, errorListener);

        VolleyRequestQueue.getInstance(this).add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(ResetPasswordResponse response) {
        if (response.mReturnCode.equals(ResetPasswordResponse.RESULT_OK)) {
            Toast.makeText(this, "重置密码成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, response.mReturnMSG, Toast.LENGTH_LONG).show();
        }
    }
}
