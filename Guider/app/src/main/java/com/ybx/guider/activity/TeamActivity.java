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
import android.widget.Toast;

import com.ybx.guider.R;
import com.ybx.guider.dialog.AcceptTeamDialog;
import com.ybx.guider.dialog.FinishTeamDialog;
import com.ybx.guider.fragment.RealNameListFragment;
import com.ybx.guider.fragment.TeamInfoFragment;
import com.ybx.guider.fragment.TeamScheduleFragment;

public class TeamActivity extends AppCompatActivity implements TeamInfoFragment.OnFragmentInteractionListener,
        AcceptTeamDialog.AcceptTeamDialogListener, FinishTeamDialog.FinishTeamDialogListener, TeamScheduleFragment.OnScheduleFragmentListener {
    public static String EXTRA_TEAM_ID = "TEAM_ID";
    private final static int NUM_ITEMS = 5;
    private PageAdapter mPageAdapter;
    private ViewPager mPager;
    private int mTeamId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

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
    public void onScheduleFragmentInteraction(Uri uri) {

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
//            return TeamInfoFragment.newInstance();

            switch(position){
                case 0:
                    return TeamInfoFragment.newInstance();
                case 1:
                    return TeamScheduleFragment.newInstance();
                case 2:
                    return RealNameListFragment.newInstance();
                case 3:
                    return TeamInfoFragment.newInstance();
                case 4:
                    return TeamInfoFragment.newInstance();
                default:
                    return null;
            }
        }
    }

    public void onClickTabInfo(View view) {
        mPager.setCurrentItem(0);
        setTitle(R.string.title_info);
    }

    public void onClickTabSchedule(View view) {
        mPager.setCurrentItem(1);
        setTitle(R.string.title_schedule);
    }

    public void onClickTabRealName(View view) {
        mPager.setCurrentItem(2);
        setTitle(R.string.title_real_name);
    }

    public void onClickTabDealRecord(View view) {
        mPager.setCurrentItem(3);
        setTitle(R.string.title_deal_record);
    }

    public void onClickTabLog(View view) {
        mPager.setCurrentItem(4);
        setTitle(R.string.title_log);
    }
}
