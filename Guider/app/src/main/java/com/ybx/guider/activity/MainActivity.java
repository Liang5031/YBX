package com.ybx.guider.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ybx.guider.R;
import com.ybx.guider.fragment.AccountVerifyFragment;
import com.ybx.guider.fragment.SettingsFragment;
import com.ybx.guider.fragment.TeamListFragement;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity implements AccountVerifyFragment.OnFragmentInteractionListener {
    private final static int NUM_ITEMS_MAIN = 4;
    private final static int NUM_ITEMS_VERIFICATION = 1;
    private final static int PAGE_ONGOING = 0;
    private final static int PAGE_WAITING = 1;
    private final static int PAGE_FINISH = 2;
    private final static int PAGE_SETTING = 3;

    //    private int mCurrentPage = PAGE_ONGOING;
    private MainAdapter mMainAdapter;
    private VerificationAdapter mVerifyAdapter;
    private ViewPager mPager;
    //    public static String EXTRA_LOGIN_RESULT_CODE = "LOGIN_RESULT_CODE";
    public static String EXTRA_ACCOUNT_STATUS = "account_status";

    private ImageView mTabOngoing;
    private ImageView mTabWaiting;
    private ImageView mTabFinish;
    private ImageView mTabSetting;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
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
        switch (id) {
            case R.id.main_change_account:
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_START_TYPE, LoginActivity.START_TYPE_CHANGE_ACCOUNT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.main_logout:
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_START_TYPE, LoginActivity.START_TYPE_LOGOUT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
//            case R.id.main_refresh:
//                Toast.makeText(this, "main_refresh pressed", Toast.LENGTH_LONG).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabOngoing = (ImageView) findViewById(R.id.tabOngoing);
        mTabWaiting = (ImageView) findViewById(R.id.tabWaiting);
        mTabFinish = (ImageView) findViewById(R.id.tabFinish);
        mTabSetting = (ImageView) findViewById(R.id.tabSetting);

        mMainAdapter = new MainAdapter(getSupportFragmentManager());
        mVerifyAdapter = new VerificationAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(1);

        Intent i = getIntent();
        String status = i.getStringExtra(EXTRA_ACCOUNT_STATUS);
        if (status == null) {
            findViewById(R.id.tabs).setVisibility(View.VISIBLE);
            setTitle(R.string.app_name_short);
            mPager.setAdapter(mMainAdapter);
            mPager.setCurrentItem(PreferencesUtils.getLastPageIndex(this), false);
            setSelectedItem(PreferencesUtils.getLastPageIndex(this));
        } else if (status != null && status.equals(ResponseUtils.ACCOUNT_STATUS_ACTIVE)) {
            findViewById(R.id.tabs).setVisibility(View.VISIBLE);
            setTitle(R.string.app_name_short);
            mPager.setAdapter(mMainAdapter);
            mPager.setCurrentItem(PAGE_ONGOING, false);
            setSelectedItem(PAGE_ONGOING);
        } else if (status != null && status.equals(ResponseUtils.ACCOUNT_STATUS_CHECKING)) {
            findViewById(R.id.tabs).setVisibility(View.GONE);
            setTitle(R.string.verify_title);
            mPager.setAdapter(mVerifyAdapter);
        }

        mPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0) {
                            setSelectedItem(PAGE_ONGOING);
                        } else if (position == 1) {
                            setSelectedItem(PAGE_WAITING);
                        } else if (position == 2) {
                            setSelectedItem(PAGE_FINISH);
                        } else if (position == 3) {
                            setSelectedItem(PAGE_SETTING);
                        }
                    }
                });
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

    public class MainAdapter extends FragmentPagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS_MAIN;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_ONGOING:
                    return TeamListFragement.newInstance(TeamListFragement.TEAM_STATUS_ONGOING);
                case PAGE_WAITING:
                    return TeamListFragement.newInstance(TeamListFragement.TEAM_STATUS_WAITING);
                case PAGE_FINISH:
                    return TeamListFragement.newInstance(TeamListFragement.TEAM_STATUS_FINISHED);
                case PAGE_SETTING:
                    return SettingsFragment.newInstance();
                default:
                    return null;
            }
        }
    }

    public void onClickOngoingTab(View view) {
        mPager.setCurrentItem(PAGE_ONGOING, false);
        setSelectedItem(PAGE_ONGOING);
    }

    public void onClickWaitingTab(View view) {
        mPager.setCurrentItem(PAGE_WAITING, false);
        setSelectedItem(PAGE_WAITING);
    }

    public void onClickFinishTab(View view) {
        mPager.setCurrentItem(PAGE_FINISH, false);
        setSelectedItem(PAGE_FINISH);
    }

    public void onClickSettingsTab(View view) {
        mPager.setCurrentItem(PAGE_SETTING, false);
        setSelectedItem(PAGE_SETTING);
    }

    public void setSelectedItem(int index) {
        PreferencesUtils.setLastPageIndex(this, index);
        switch (index) {
            case PAGE_ONGOING:
                mTabOngoing.setImageResource(R.mipmap.ongoing_pressed);
                mTabWaiting.setImageResource(R.mipmap.waiting);
                mTabFinish.setImageResource(R.mipmap.finish);
                mTabSetting.setImageResource(R.mipmap.setting);
                setTitle("正在带的团");
                break;
            case PAGE_WAITING:
                mTabOngoing.setImageResource(R.mipmap.ongoing);
                mTabWaiting.setImageResource(R.mipmap.waiting_pressed);
                mTabFinish.setImageResource(R.mipmap.finish);
                mTabSetting.setImageResource(R.mipmap.setting);
                setTitle("等待我带的团");
                break;
            case PAGE_FINISH:
                mTabOngoing.setImageResource(R.mipmap.ongoing);
                mTabWaiting.setImageResource(R.mipmap.waiting);
                mTabFinish.setImageResource(R.mipmap.finish_pressed);
                mTabSetting.setImageResource(R.mipmap.setting);
                setTitle("已完成的团");
                break;
            case PAGE_SETTING:
                mTabOngoing.setImageResource(R.mipmap.ongoing);
                mTabWaiting.setImageResource(R.mipmap.waiting);
                mTabFinish.setImageResource(R.mipmap.finish);
                mTabSetting.setImageResource(R.mipmap.setting_pressed);
                setTitle("设置");
                break;
            default:
                break;
        }

    }

}
