package com.ybx.guider.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ybx.guider.R;
import com.ybx.guider.dialog.AcceptTeamDialog;
import com.ybx.guider.dialog.FinishTeamDialog;
import com.ybx.guider.fragment.DealRecordListFragment;
import com.ybx.guider.fragment.RealNameListFragment;
import com.ybx.guider.fragment.TeamInfoFragment;
import com.ybx.guider.fragment.TeamLogListFragment;
import com.ybx.guider.fragment.TeamScheduleFragment;

public class TeamActivity extends AppCompatActivity implements TeamInfoFragment.OnFragmentInteractionListener,
        AcceptTeamDialog.AcceptTeamDialogListener, FinishTeamDialog.FinishTeamDialogListener {
    private final static int PAGE_MAIN_INFO = 0;
    private final static int PAGE_TEAM_SCHEDULE = 1;
    private final static int PAGE_TEAM_REAL_NAME = 2;
    private final static int PAGE_TEAM_DEAL_RECORD = 3;
    private final static int PAGE_TEAM_LOG = 4;
    public static String EXTRA_TEAM_ID = "TEAM_ID";
    private final static int NUM_ITEMS = 5;
    private PageAdapter mPageAdapter;
    private ViewPager mPager;
    private int mTeamId = -1;

    private ImageView mTabMainInfo;
    private ImageView mTabTeamSchedule;
    private ImageView mTabTeamDealRecord;
    private ImageView mTabTeamRealName;
    private ImageView mTabTeamLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        mTabMainInfo = (ImageView)findViewById(R.id.tabMainInfo);
        mTabTeamSchedule = (ImageView)findViewById(R.id.tabTeamSchedule);
        mTabTeamDealRecord = (ImageView)findViewById(R.id.tabTeamDealRecord);
        mTabTeamRealName = (ImageView)findViewById(R.id.tabTeamRealName);
        mTabTeamLog = (ImageView)findViewById(R.id.tabTeamLog);

        Intent i = getIntent();
        mTeamId = i.getIntExtra(EXTRA_TEAM_ID, -1);

        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                    }
                });

        mPager.setAdapter(mPageAdapter);
    }

    @Override
    public void onAcceptTeamDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(this, "OK pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAcceptTeamDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this, "Cancel pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFinishTeamDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(this, "OK pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFinishTeamDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this, "Cancel pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class PageAdapter extends FragmentPagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case PAGE_MAIN_INFO:
                    return TeamInfoFragment.newInstance();
                case PAGE_TEAM_SCHEDULE:
                    return TeamScheduleFragment.newInstance();
                case PAGE_TEAM_REAL_NAME:
                    return RealNameListFragment.newInstance();
                case PAGE_TEAM_DEAL_RECORD:
                    return DealRecordListFragment.newInstance();
                case PAGE_TEAM_LOG:
                    return TeamLogListFragment.newInstance();
                default:
                    return null;
            }
        }
    }

    public void onClickTabInfo(View view) {
        mPager.setCurrentItem(PAGE_MAIN_INFO);
        setSelectedItem(PAGE_MAIN_INFO);
        setTitle(R.string.title_info);
    }

    public void onClickTabSchedule(View view) {
        mPager.setCurrentItem(PAGE_TEAM_SCHEDULE);
        setSelectedItem(PAGE_TEAM_SCHEDULE);
        setTitle(R.string.title_schedule);
    }

    public void onClickTabRealName(View view) {
        mPager.setCurrentItem(PAGE_TEAM_REAL_NAME);
        setSelectedItem(PAGE_TEAM_REAL_NAME);
        setTitle(R.string.title_real_name);
    }

    public void onClickTabDealRecord(View view) {
        mPager.setCurrentItem(PAGE_TEAM_DEAL_RECORD);
        setSelectedItem(PAGE_TEAM_DEAL_RECORD);
        setTitle(R.string.title_deal_record);
    }

    public void onClickTabLog(View view) {
        mPager.setCurrentItem(PAGE_TEAM_LOG);
        setSelectedItem(PAGE_TEAM_LOG);
        setTitle(R.string.title_log);
    }

    public void setSelectedItem(int index){
        switch (index){
            case PAGE_MAIN_INFO:
                mTabMainInfo.setImageResource(R.mipmap.main_info_pressed);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                break;
            case PAGE_TEAM_SCHEDULE:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule_pressed);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                break;
            case PAGE_TEAM_REAL_NAME:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name_pressed);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                break;
            case PAGE_TEAM_DEAL_RECORD:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record_pressed);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                break;
            case PAGE_TEAM_LOG:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log_pressed);
                break;
            default:
                break;
        }
    }
}
