package com.ybx.guider.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ybx.guider.R;
import com.ybx.guider.VolleyRequestQueue;
import com.ybx.guider.fragment.PhoneVerifyFragment;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
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
//        tv.setText("That didn't work!");
    }

    @Override
    public void onResponse(String response) {
        // Display the first 500 characters of the response string.
//        tv.setText("Response is: "+ response.substring(0,500));
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickRemember(View view) {
//        Intent intent = new Intent(this, ResetPasswordActivity.class);
//        startActivity(intent);
    }

}
