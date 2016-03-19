package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.ybx.guider.responses.LoginResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
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
    XMLRequest<LoginResponse> mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mIsAutoLogin = (CheckBox) findViewById(R.id.isAutoLogin);
        mPassword = (EditText) findViewById(R.id.password);
        mGuiderNumber = (EditText) findViewById(R.id.guiderNumber);

        mGuiderNumber.setText(PreferencesUtils.getGuiderNumber(this));
        mPassword.setText(PreferencesUtils.getPassword(this));
        mIsAutoLogin.setChecked(PreferencesUtils.getIsAutoLogin(this));

        Intent intent = getIntent();
        String loginType = intent.getStringExtra(EXTRA_START_TYPE);
        if (loginType != null && loginType.equals(START_TYPE_LOGOUT)) {
            PreferencesUtils.clearLoginInfo(this);
        } else if (PreferencesUtils.getIsAutoLogin(this) && (loginType == null || !loginType.equals(START_TYPE_CHANGE_ACCOUNT))) {
            reqeustLogin(PreferencesUtils.getGuiderNumber(this), PreferencesUtils.getPassword(this));
//            this.finish();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRequest!=null){
            mRequest.cancel();
        }
    }

    public void onClickForgot(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
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
        PreferencesUtils.setIsAutoLogin(this,mIsAutoLogin.isChecked());
    }

    private boolean inputCheck() {
        if (mGuiderNumber == null || mGuiderNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "导游证号不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPassword == null || mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void reqeustLogin(String guiderNumber, String password) {
        if (guiderNumber == null || guiderNumber.isEmpty()) {
            return;
        }
        if (password == null || password.isEmpty()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "正在登录", "请稍等...", true, false);

        Param param = new Param(ParamUtils.PAGE_GUIDER_LOGIN);
        param.setUser(guiderNumber);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, password);
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<LoginResponse>(url, this, this, new LoginResponse());
        mRequest.setShouldCache(false);

/*
        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
*/

        VolleyRequestQueue.getInstance(this).add(mRequest);

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        this.finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        Toast.makeText(this, "登录失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(LoginResponse response) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK) && response.mAccountStatus!=null) {
            /* clear cache after login */
            VolleyRequestQueue.getInstance(this).clear();

            if (response.mAccountStatus.equalsIgnoreCase(ResponseUtils.ACCOUNT_STATUS_INACTIVE)) {
                Toast.makeText(this, "账号已禁用！", Toast.LENGTH_LONG).show();
            } else if (response.mAccountStatus.equalsIgnoreCase(ResponseUtils.ACCOUNT_STATUS_ACTIVE)
                    || response.mAccountStatus.equalsIgnoreCase(ResponseUtils.ACCOUNT_STATUS_CHECKING)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_ACCOUNT_STATUS, response.mAccountStatus);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(this, "注册审核未通过！", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, response.mReturnMSG, Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }
}
