package com.ybx.guider.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.Utils;
import com.ybx.guider.VolleyRequestQueue;
import com.ybx.guider.parameters.LoginParam;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.LoginRequest;
import com.ybx.guider.responses.LoginResponse;

public class LoginActivity extends AppCompatActivity implements Response.Listener<LoginResponse>, Response.ErrorListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


//        String url ="http://www.baidu.com";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,this,this);
//        VolleyRequestQueue.getInstance(this).add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(LoginResponse response) {
        if (response.mReturnCode.equals(LoginResponse.RESULT_OK)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,response.mReturnMSG,Toast.LENGTH_LONG).show();
        }
    }

    public void onClickForgot(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View view) {
        LoginParam param = new LoginParam();
        param.setUser("user");
        LoginRequest request = new LoginRequest(Utils.generateURL(ParamUtils.PAGE_LOGIN, param), this, this);

//        String url = "http://www.baidu.com";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        VolleyRequestQueue.getInstance(this).add(request);
    }

    public void onClickRemember(View view) {
//        Intent intent = new Intent(this, ResetPasswordActivity.class);
//        startActivity(intent);
    }

}
