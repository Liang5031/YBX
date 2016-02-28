package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.LoginParam;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.LoginRequest;
import com.ybx.guider.responses.BaseResponse;
import com.ybx.guider.responses.LoginResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

public class LoginActivity extends AppCompatActivity implements Response.Listener<LoginResponse>, Response.ErrorListener {
    private TextView tv;
    private EditText mPassword;
    private EditText mGuiderNumber;
    private CheckBox mIsAutoLogin;
    private ProgressDialog mProgressDialog;
    public static String EXTRA_START_TYPE = "LOGIN_TYPE";
    public static String START_TYPE_CHANGE_ACCOUNT = "CHANGE_ACCOUNT";
    public static String START_TYPE_LOGOUT = "LOGOUT";
    public static String START_TYPE_DEFAULT = "DEFAULT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent intent = getIntent();
        String loginType = intent.getStringExtra(EXTRA_START_TYPE);
        if (loginType != null && loginType.equals(START_TYPE_LOGOUT)) {
            PreferencesUtils.clearLoginInfo(this);
        } else if (PreferencesUtils.getIsAutoLogin(this) && (loginType == null || !loginType.equals(START_TYPE_CHANGE_ACCOUNT))) {
            reqeustLogin(PreferencesUtils.getGuiderNumber(this), PreferencesUtils.getPassword(this));
//            this.finish();
            return;
        }

        mIsAutoLogin = (CheckBox) findViewById(R.id.isAutoLogin);
        mPassword = (EditText) findViewById(R.id.password);
        mGuiderNumber = (EditText) findViewById(R.id.guiderNumber);

        mGuiderNumber.setText(PreferencesUtils.getGuiderNumber(this));
        mPassword.setText(PreferencesUtils.getPassword(this));
        mIsAutoLogin.setChecked(PreferencesUtils.getIsAutoLogin(this));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(mProgressDialog!=null&& mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(this, "登录失败！", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onResponse(LoginResponse response) {
        if(mProgressDialog!=null&& mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (response.mReturnCode.equals(BaseResponse.RESULT_OK)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_LOGIN_RESULT_CODE, response.mReturnCode);
//            intent.putExtra(MainActivity.EXTRA_LOGIN_RESULT_CODE, BaseResponse.RESULT_FAIL);
            intent.putExtra(MainActivity.EXTRA_LOGIN_RESULT_MSG, response.mReturnMSG);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(this, response.mReturnMSG, Toast.LENGTH_LONG).show();
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
        if (inputCheck()) {
            PreferencesUtils.saveLoginInfo(this, mGuiderNumber.getText().toString(), mPassword.getText().toString(), mIsAutoLogin.isChecked());
            reqeustLogin(mGuiderNumber.getText().toString(), mPassword.getText().toString());
        }
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    public void onClickAutoLogin(View view) {
//        setIsAutoLogin(mIsAutoLogin.isChecked());
    }

    private boolean inputCheck() {
        if (mGuiderNumber == null || mGuiderNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入导游证号！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPassword == null || mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入密码！", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void reqeustLogin(String guiderNumber, String password) {
        if (guiderNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "导游证号和密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "正在登录", "请稍等...", true, false);

        LoginParam param = new LoginParam();
        param.setUser(guiderNumber);
        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, password);
        param.setSign(sign);

        LoginRequest request = new LoginRequest(URLUtils.generateURL(ParamUtils.PAGE_LOGIN, param), this, this);

        VolleyRequestQueue.getInstance(this).add(request);
    }
}
