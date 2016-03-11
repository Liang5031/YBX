package com.ybx.guider.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ybx.guider.R;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.setTitle("修改密码");
//        ActionBar ab = getSupportActionBar();
//
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);
    }


    public void onClickChangePassword(View view){

    }

    public void onClickRememberPassword(View view){

    }
}
