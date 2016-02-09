package com.ybx.guider.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.ybx.guider.R;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passward);
        this.setTitle(R.string.reset_password);
    }
}
