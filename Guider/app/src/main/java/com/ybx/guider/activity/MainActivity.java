package com.ybx.guider.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ybx.guider.R;
import com.ybx.guider.fragment.PhoneVerifyFragment;

public class MainActivity extends AppCompatActivity implements PhoneVerifyFragment.OnFragmentInteractionListener {
    private final static int NUM_ITEMS = 4;
    private MyAdapter mAdapter;
    private ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                    }
                });
    }

    @Override
    public void onPhoneVerified(Uri uri) {

    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return new PhoneVerifyFragment();
//            switch(position){
//                case 1:
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
//            }
//            return null;
        }
    }

    public void onClickOngoingTab(View view){
        mPager.setCurrentItem(0);
    }

    public void onClickWaitingTab(View view){
        mPager.setCurrentItem(1);
    }

    public void onClickFinishTab(View view){
        mPager.setCurrentItem(2);
    }

    public void onClickSettingsTab(View view){
        mPager.setCurrentItem(3);
    }

}
