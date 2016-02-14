package com.ybx.guider.activity;

//import android.app.FragmentTransaction;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.fragment.InformationFragment;
import com.ybx.guider.fragment.PhoneVerifyFragment;
import com.ybx.guider.fragment.UploadPhotoFragment;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity implements UploadPhotoFragment.OnFragmentInteractionListener, PhoneVerifyFragment.OnFragmentInteractionListener, InformationFragment.OnFragmentInteractionListener {
    public static final int STEP_ONE = 1;
    public static final int STEP_TWO = 2;
    public static final int STEP_THREE = 3;
    private TextView mTVPrev;
    private TextView mTVNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loadFragment(new UploadPhotoFragment(), false);
        this.setTitle(R.string.sign_up_title);
        mTVPrev = (TextView)findViewById(R.id.Prev);
        mTVNext = (TextView)findViewById(R.id.Next);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStep(STEP_ONE);
    }

    void loadFragment(Fragment f, boolean addToBackStack){
        // Create new fragment and transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, f);
        if(addToBackStack) {
            transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onPhoneVerified(Uri uri) {

    }

    @Override
    public void onPhotoUploaded(Uri uri) {

    }

    @Override
    public void onSubmit(Uri uri) {

    }

    public void onClickNext(View view){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if( f instanceof UploadPhotoFragment){
            loadFragment(new PhoneVerifyFragment(), false);
            setStep(STEP_TWO);
        } else if(f instanceof PhoneVerifyFragment){
            loadFragment(new InformationFragment(), false);
            setStep(STEP_THREE);
        }
    }

    public void onClickPrev(View view){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if( f instanceof InformationFragment){
            loadFragment(new PhoneVerifyFragment(), false);
            setStep(STEP_TWO);
        } else if(f instanceof PhoneVerifyFragment){
            loadFragment(new UploadPhotoFragment(), false);
            setStep(STEP_ONE);
        }
    }

    private void setStep(int step){
        TextView stepName = (TextView)findViewById(R.id.stepName);
        switch (step){
            case STEP_ONE:
                stepName.setText(R.string.subject_step1);
                findViewById(R.id.Prev).setVisibility(View.GONE);
                findViewById(R.id.Next).setVisibility(View.VISIBLE);
                break;
            case STEP_TWO:
                stepName.setText(R.string.subject_step2);
                findViewById(R.id.Prev).setVisibility(View.VISIBLE);
                findViewById(R.id.Next).setVisibility(View.VISIBLE);
                break;
            case STEP_THREE:
                stepName.setText(R.string.subject_step3);
                findViewById(R.id.Prev).setVisibility(View.VISIBLE);
                findViewById(R.id.Next).setVisibility(View.GONE);
                break;
        }
    }
}
