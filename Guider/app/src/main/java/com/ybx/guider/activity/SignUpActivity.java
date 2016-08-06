package com.ybx.guider.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ybx.guider.R;
import com.ybx.guider.fragment.PhoneVerifyFragment;
import com.ybx.guider.fragment.SignUpInfoFragment;
import com.ybx.guider.fragment.UploadPhotoFragment;
import com.ybx.guider.utils.PreferencesUtils;

public class SignUpActivity extends AppCompatActivity implements UploadPhotoFragment.OnFragmentInteractionListener, PhoneVerifyFragment.OnFragmentInteractionListener, SignUpInfoFragment.OnFragmentInteractionListener {
    public static final int STEP_ONE = 1;
    public static final int STEP_TWO = 2;
    public static final int STEP_THREE = 3;
    private TextView mTVPrev;
    private TextView mTVNext;
    private String mVerifyCode;
    private String mPhoneNumber;
    private boolean mIsPhotoUploaded = false;

    PhoneVerifyFragment mPhoneVerifyFragment;
    UploadPhotoFragment mUploadPhotoFragment;
    SignUpInfoFragment mSignUpInfoFragment;
    int mCurrentStep = STEP_ONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSignUpInfoFragment = SignUpInfoFragment.newInstance();
        mPhoneVerifyFragment = PhoneVerifyFragment.newInstance();
        mUploadPhotoFragment = UploadPhotoFragment.newInstance(PreferencesUtils.getGuiderNumber(this));
        loadFragment(mUploadPhotoFragment, false);

        this.setTitle(R.string.sign_up_title);
        mTVPrev = (TextView) findViewById(R.id.Prev);
        mTVNext = (TextView) findViewById(R.id.Next);



    }

    @Override
    protected void onResume() {
        super.onResume();
//        setStep(STEP_ONE);
    }

    void loadFragment(Fragment f, boolean addToBackStack) {
        // Create new fragment and transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, f);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onPhoneVerified(String phoneNumber, String verifyCode) {
        mPhoneNumber = phoneNumber;
        mVerifyCode = verifyCode;
    }

    @Override
    public void onPhotoUploaded(boolean isUploaded) {
        mIsPhotoUploaded = isUploaded;
    }

    @Override
    public void onSubmit(Uri uri) {

    }

    public void onClickNext(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (f instanceof UploadPhotoFragment ) {
            if(!mIsPhotoUploaded){
                Toast.makeText(SignUpActivity.this, "请先上传照片！", Toast.LENGTH_SHORT).show();
            } else {
                loadFragment(mPhoneVerifyFragment, false);
                setStep(STEP_TWO);
            }
        } else if (f instanceof PhoneVerifyFragment) {
            if (mVerifyCode == null || mVerifyCode.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            } else if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            } else {

                mSignUpInfoFragment.setPhoneNumber(mPhoneNumber);
                mSignUpInfoFragment.setVerifyCode(mVerifyCode);
                loadFragment(mSignUpInfoFragment, false);
                setStep(STEP_THREE);
            }
        }
    }

    public void onClickPrev(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (f instanceof SignUpInfoFragment) {
            loadFragment(mPhoneVerifyFragment, false);
            setStep(STEP_TWO);
        } else if (f instanceof PhoneVerifyFragment) {
            loadFragment(mUploadPhotoFragment, false);
            setStep(STEP_ONE);
        }
    }

    private void setStep(int step) {
        TextView stepName = (TextView) findViewById(R.id.stepName);
        switch (step) {
            case STEP_ONE:
                stepName.setText(R.string.subject_step1);
                findViewById(R.id.Prev).setVisibility(View.GONE);
                findViewById(R.id.Next).setVisibility(View.VISIBLE);
                mCurrentStep = STEP_ONE;
                break;
            case STEP_TWO:
                stepName.setText(R.string.subject_step2);
                findViewById(R.id.Prev).setVisibility(View.VISIBLE);
                findViewById(R.id.Next).setVisibility(View.VISIBLE);
                mCurrentStep = STEP_TWO;
                break;
            case STEP_THREE:
                stepName.setText(R.string.subject_step3);
                findViewById(R.id.Prev).setVisibility(View.VISIBLE);
                findViewById(R.id.Next).setVisibility(View.GONE);
                mCurrentStep = STEP_THREE;
                break;
        }
    }
}
