package com.ybx.guider.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.ybx.guider.R;
import com.ybx.guider.fragment.AccountVerifyFragment;
import com.ybx.guider.fragment.TeamListFragement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements AccountVerifyFragment.OnFragmentInteractionListener{
    private final static int NUM_ITEMS_MAIN = 4;
    private final static int NUM_ITEMS_VERIFICATION = 1;
    private MainAdapter mMainAdapter;
    private VerificationAdapter mVerifyAdapter;
    private ViewPager mPager;
    public static String EXTRA_LOGIN_RESULT_CODE = "LOGIN_RESULT_CODE";
    public static String EXTRA_LOGIN_RESULT_MSG = "LOGIN_RESULT_MSG";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.main_change_account:
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_START_TYPE, LoginActivity.START_TYPE_CHANGE_ACCOUNT);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.main_logout:
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_START_TYPE, LoginActivity.START_TYPE_LOGOUT);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.main_refresh:
                Toast.makeText(this, "main_refresh pressed", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainAdapter = new MainAdapter(getSupportFragmentManager());
        mVerifyAdapter = new VerificationAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.viewpager);
        Intent i = getIntent();
        String resultCode = i.getStringExtra(EXTRA_LOGIN_RESULT_CODE);
//        if (resultCode!= null && resultCode.equals(BaseResponse.RESULT_OK)) {
            findViewById(R.id.radioTabs).setVisibility(View.VISIBLE);
            setTitle(R.string.app_name_short);
            mPager.setAdapter(mMainAdapter);
//        } else if(resultCode!= null && resultCode.equals(BaseResponse.RESULT_FAIL)) {
//            findViewById(R.id.radioTabs).setVisibility(View.GONE);
//            setTitle(R.string.verify_title);
//            mPager.setAdapter(mVerifyAdapter);
//        }

        mPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                    }
                });


//        ActionBar actionBar = getSupportActionBar();
//        // 是否显示应用程序图标，默认为true
//        actionBar.setDisplayShowHomeEnabled(true);
//        // 是否显示应用程序标题，默认为true
//        actionBar.setDisplayShowTitleEnabled(true);
//
//        /*
//         * 是否将应用程序图标转变成可点击的按钮，默认为false。
//         *
//         * 如果设置了DisplayHomeAsUpEnabled为true，
//         *
//         * 则该设置自动为 true。
//         */
//        actionBar.setHomeButtonEnabled(true);
//        /*
//         * 在应用程序图标的左边显示一个向左的箭头，
//         *
//         * 并且将HomeButtonEnabled设为true。
//         *
//         * 默认为false。
//         */
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        forceShowOverflowMenu();
    }

    /**
     * 如果设备有物理菜单按键，需要将其屏蔽才能显示OverflowMenu
     */
    private void forceShowOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示OverflowMenu的Icon
     *
     * @param featureId
     * @param menu
     */
    private void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class VerificationAdapter extends FragmentPagerAdapter {
        public VerificationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS_VERIFICATION;
        }

        @Override
        public Fragment getItem(int position) {
            return AccountVerifyFragment.newInstance();
        }
    }

    public static class MainAdapter extends FragmentPagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS_MAIN;
        }

        @Override
        public Fragment getItem(int position) {
            return new TeamListFragement();
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

    public void onClickOngoingTab(View view) {
        mPager.setCurrentItem(0);
    }

    public void onClickWaitingTab(View view) {
        mPager.setCurrentItem(1);
    }

    public void onClickFinishTab(View view) {
        mPager.setCurrentItem(2);
    }

    public void onClickSettingsTab(View view) {
        mPager.setCurrentItem(3);
    }

}
