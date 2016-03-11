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
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamScheduleItem;

public class TeamActivity extends AppCompatActivity implements TeamInfoFragment.OnFragmentInteractionListener,
        AcceptTeamDialog.AcceptTeamDialogListener, FinishTeamDialog.FinishTeamDialogListener {
    private final static int PAGE_MAIN_INFO = 0;
    private final static int PAGE_TEAM_SCHEDULE = 1;
    private final static int PAGE_TEAM_REAL_NAME = 2;
    private final static int PAGE_TEAM_DEAL_RECORD = 3;
    private final static int PAGE_TEAM_LOG = 4;
    public static String EXTRA_TEAM_ITEM = "team_item";
    private final static int NUM_ITEMS = 5;
    private PageAdapter mPageAdapter;
    private ViewPager mPager;
    //    public int mTeamId = -1;
    public TeamItem mTeamItem;

    private ImageView mTabMainInfo;
    private ImageView mTabTeamSchedule;
    private ImageView mTabTeamDealRecord;
    private ImageView mTabTeamRealName;
    private ImageView mTabTeamLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        mTabMainInfo = (ImageView) findViewById(R.id.tabMainInfo);
        mTabTeamSchedule = (ImageView) findViewById(R.id.tabTeamSchedule);
        mTabTeamDealRecord = (ImageView) findViewById(R.id.tabTeamDealRecord);
        mTabTeamRealName = (ImageView) findViewById(R.id.tabTeamRealName);
        mTabTeamLog = (ImageView) findViewById(R.id.tabTeamLog);

        Intent i = getIntent();
        mTeamItem = (TeamItem) i.getSerializableExtra(EXTRA_TEAM_ITEM);

        mPageAdapter = new PageAdapter(getSupportFragmentManager(),mTeamItem);
        mPager = (ViewPager) findViewById(R.id.viewpager);
//        mPager.setOffscreenPageLimit();
        mPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0) {
                            setSelectedItem(PAGE_MAIN_INFO);
                        } else if (position == 1) {
                            setSelectedItem(PAGE_TEAM_SCHEDULE);
                        } else if (position == 2) {
                            setSelectedItem(PAGE_TEAM_REAL_NAME);
                        } else if (position == 3) {
                            setSelectedItem(PAGE_TEAM_DEAL_RECORD);
                        }else if (position == 4) {
                            setSelectedItem(PAGE_TEAM_LOG);
                        }
                    }
                });

        mPager.setAdapter(mPageAdapter);
        setSelectedItem(PAGE_MAIN_INFO);
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
        TeamItem mItem;

        public PageAdapter(FragmentManager fm, TeamItem item) {
            super(fm);
            mItem = item;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_MAIN_INFO:
                    return TeamInfoFragment.newInstance();
                case PAGE_TEAM_SCHEDULE:
                    return TeamScheduleFragment.newInstance(mItem);
                case PAGE_TEAM_REAL_NAME:
                    return RealNameListFragment.newInstance(mItem);
                case PAGE_TEAM_DEAL_RECORD:
                    return DealRecordListFragment.newInstance(mItem);
                case PAGE_TEAM_LOG:
                    return TeamLogListFragment.newInstance(mItem);
                default:
                    return null;
            }
        }
    }

    public void onClickTabInfo(View view) {
        mPager.setCurrentItem(PAGE_MAIN_INFO);
        setSelectedItem(PAGE_MAIN_INFO);
    }

    public void onClickTabSchedule(View view) {
        mPager.setCurrentItem(PAGE_TEAM_SCHEDULE);
        setSelectedItem(PAGE_TEAM_SCHEDULE);
    }

    public void onClickTabRealName(View view) {
        mPager.setCurrentItem(PAGE_TEAM_REAL_NAME);
        setSelectedItem(PAGE_TEAM_REAL_NAME);
    }

    public void onClickTabDealRecord(View view) {
        mPager.setCurrentItem(PAGE_TEAM_DEAL_RECORD);
        setSelectedItem(PAGE_TEAM_DEAL_RECORD);
    }

    public void onClickTabLog(View view) {
        mPager.setCurrentItem(PAGE_TEAM_LOG);
        setSelectedItem(PAGE_TEAM_LOG);
    }

    public void setSelectedItem(int index) {
        switch (index) {
            case PAGE_MAIN_INFO:
                mTabMainInfo.setImageResource(R.mipmap.main_info_pressed);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                setTitle(R.string.title_info);
                break;
            case PAGE_TEAM_SCHEDULE:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule_pressed);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                setTitle(R.string.title_schedule);
                break;
            case PAGE_TEAM_REAL_NAME:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name_pressed);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                setTitle(R.string.title_real_name);
                break;
            case PAGE_TEAM_DEAL_RECORD:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record_pressed);
                mTabTeamLog.setImageResource(R.mipmap.team_log);
                setTitle(R.string.title_deal_record);
                break;
            case PAGE_TEAM_LOG:
                mTabMainInfo.setImageResource(R.mipmap.main_info);
                mTabTeamSchedule.setImageResource(R.mipmap.team_schedule);
                mTabTeamRealName.setImageResource(R.mipmap.team_real_name);
                mTabTeamDealRecord.setImageResource(R.mipmap.team_deal_record);
                mTabTeamLog.setImageResource(R.mipmap.team_log_pressed);
                setTitle(R.string.title_log);
                break;
            default:
                break;
        }
    }

    public void onClickFinish(View view){
        Toast.makeText(this, "onClickFinish", Toast.LENGTH_LONG).show();
    }

    public void onClickCancelSchedule(View view){
        Toast.makeText(this, "onClickCancelSchedule", Toast.LENGTH_LONG).show();
    }

    public void onClickStartAppo(View view){
        TeamScheduleItem item = (TeamScheduleItem)view.getTag();
        Intent intent = new Intent(this, ReservationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReservationActivity.EXTRA_TEAM_ITEM, mTeamItem);
        bundle.putSerializable(ReservationActivity.EXTRA_TEAM_SCHEDULE_ITEM, item);
        intent.putExtras(bundle);
        startActivity(intent);

//        Toast.makeText(this, "onClickStartAppo", Toast.LENGTH_LONG).show();
    }

    public void onClickCancelAppo(View view){
        Toast.makeText(this, "onClickCancelAppo", Toast.LENGTH_LONG).show();
    }

    public void onClickChangeAppo(View view){
        Toast.makeText(this, "onClickChangeAppo", Toast.LENGTH_LONG).show();
    }

    public void onClickSync(View view){
        Toast.makeText(this, "onClickSync", Toast.LENGTH_LONG).show();
    }
}
