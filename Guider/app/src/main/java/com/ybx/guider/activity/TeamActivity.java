package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.dialog.AcceptTeamDialog;
import com.ybx.guider.dialog.FinishTeamDialog;
import com.ybx.guider.fragment.DealRecordListFragment;
import com.ybx.guider.fragment.RealNameListFragment;
import com.ybx.guider.fragment.TeamInfoFragment;
import com.ybx.guider.fragment.TeamLogListFragment;
import com.ybx.guider.fragment.TeamScheduleFragment;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamScheduleItem;
import com.ybx.guider.responses.TeamScheduleListResponse;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

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
    ArrayList<TeamScheduleItem> mAllTeamSchduleItems;
    ArrayList<TeamScheduleItem> mCancelItems;
    ArrayList<TeamScheduleItem> mSyncItems;

    private ImageView mTabMainInfo;
    private ImageView mTabTeamSchedule;
    private ImageView mTabTeamDealRecord;
    private ImageView mTabTeamRealName;
    private ImageView mTabTeamLog;

    XMLRequest<XMLResponse> mAcceptRequest;
    XMLRequest<XMLResponse> mFinishRequest;
    XMLRequest<XMLResponse> mCancelAppoRequest;
    XMLRequest<XMLResponse> mSyncAppoRequest;
    XMLRequest<TeamScheduleListResponse> mTeamScheduleListRequest;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.showOverflowMenu(this);
        setContentView(R.layout.activity_team);

        mTabMainInfo = (ImageView) findViewById(R.id.tabMainInfo);
        mTabTeamSchedule = (ImageView) findViewById(R.id.tabTeamSchedule);
        mTabTeamDealRecord = (ImageView) findViewById(R.id.tabTeamDealRecord);
        mTabTeamRealName = (ImageView) findViewById(R.id.tabTeamRealName);
        mTabTeamLog = (ImageView) findViewById(R.id.tabTeamLog);

        Intent i = getIntent();
        mTeamItem = (TeamItem) i.getSerializableExtra(EXTRA_TEAM_ITEM);

        mPageAdapter = new PageAdapter(getSupportFragmentManager(), mTeamItem);
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
                        } else if (position == 4) {
                            setSelectedItem(PAGE_TEAM_LOG);
                        }
                    }
                });

        mPager.setAdapter(mPageAdapter);
        setSelectedItem(PAGE_MAIN_INFO);

        mAllTeamSchduleItems = new ArrayList<TeamScheduleItem>();
        mCancelItems = new ArrayList<TeamScheduleItem>();
        mSyncItems = new ArrayList<TeamScheduleItem>();
    }

    @Override
    public void onAcceptTeamDialogOK(DialogFragment dialog) {
        requestAcceptTeam();
    }

    @Override
    public void onAcceptTeamDialogCancel(DialogFragment dialog) {
    }

    @Override
    public void onFinishTeamDialogOK(DialogFragment dialog) {
        if(dialog instanceof FinishTeamDialog) {
            FinishTeamDialog d = (FinishTeamDialog)dialog;
            if(d.mConfirmation.getText().toString().isEmpty() || !d.mConfirmation.getText().toString().equalsIgnoreCase("OK")){
                Toast.makeText(this, "请先输入OK！", Toast.LENGTH_LONG).show();
            } else {
                mProgressDialog = ProgressDialog.show(this, "正在完成带团", "请稍等...", true, false);
                requestTeamScheduleList(mTeamItem.TeamIndex, ParamUtils.VALUE_FIRST_PAGE_INDEX, true);
            }
        }
    }

    @Override
    public void onFinishTeamDialogCancel(DialogFragment dialog) {

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

    public void onClickFinish(View view) {
//        Toast.makeText(this, "onClickFinish", Toast.LENGTH_LONG).show();
    }

    public void onClickCancelSchedule(View view) {
//        Toast.makeText(this, "onClickCancelSchedule", Toast.LENGTH_LONG).show();
    }

//    public void onClickStartAppo(View view) {
//        TeamScheduleItem item = (TeamScheduleItem) view.getTag();
//        Intent intent = new Intent(this, ReservationActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt(ReservationActivity.EXTRA_RESERVATION_TYPE, ReservationActivity.TYPE_START);
//        bundle.putSerializable(ReservationActivity.EXTRA_TEAM_ITEM, mTeamItem);
//        bundle.putSerializable(ReservationActivity.EXTRA_TEAM_SCHEDULE_ITEM, item);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }

    public void onClickCancelAppo(View view) {
        TeamScheduleItem item = (TeamScheduleItem) view.getTag();
        requestCancelAppo(item);
    }

//    public void onClickChangeAppo(View view) {
//        TeamScheduleItem item = (TeamScheduleItem) view.getTag();
//        Intent intent = new Intent(this, ReservationActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt(ReservationActivity.EXTRA_RESERVATION_TYPE, ReservationActivity.TYPE_CHANGE);
//        bundle.putSerializable(ReservationActivity.EXTRA_TEAM_ITEM, mTeamItem);
//        bundle.putSerializable(ReservationActivity.EXTRA_TEAM_SCHEDULE_ITEM, item);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }

//    public void onClickSync(View view) {
//        TeamScheduleItem item = (TeamScheduleItem) view.getTag();
//        reqeustSync(item);
//        reqeustSync2(item);
//    }

    void requestAcceptTeam() {
        Param param = new Param(ParamUtils.PAGE_TEAM_ACCEPT);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTeamIndex(mTeamItem.TeamIndex);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> listener = new Response.Listener<XMLResponse>() {
            @Override
            public void onResponse(XMLResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(TeamActivity.this, "接团成功!", Toast.LENGTH_LONG).show();
                    TeamActivity.this.finish();
                } else {
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeamActivity.this, "接团失败!", Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mAcceptRequest = new XMLRequest<XMLResponse>(url, listener, errorListener, new XMLResponse());
        mAcceptRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mAcceptRequest);
    }

    void requestFinishTeam() {
        Param param = new Param(ParamUtils.PAGE_TEAM_COMPLETE);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTeamIndex(mTeamItem.TeamIndex);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> listener = new Response.Listener<XMLResponse>() {
            @Override
            public void onResponse(XMLResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(TeamActivity.this, "完成带团成功!", Toast.LENGTH_LONG).show();
                    TeamActivity.this.finish();
                } else {
                    if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(TeamActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mFinishRequest = new XMLRequest<XMLResponse>(url, listener, errorListener, new XMLResponse());
        mFinishRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mFinishRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFinishRequest != null) {
            mFinishRequest.cancel();
        }

        if (mAcceptRequest != null) {
            mAcceptRequest.cancel();
        }

        if (mCancelAppoRequest != null) {
            mCancelAppoRequest.cancel();
        }

        if (mSyncAppoRequest != null) {
            mSyncAppoRequest.cancel();
        }

        if (mTeamScheduleListRequest != null) {
            mTeamScheduleListRequest.cancel();
        }
    }

    void requestCancelAppo(TeamScheduleItem item) {
        Param param = new Param(ParamUtils.PAGE_APPOINTMENT_CANCEL);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTripItemIndex(item.getTripIndex());
        param.addParam(ParamUtils.KEY_RESERVATION_NUMBER, item.getAppoNumber());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> listener = new Response.Listener<XMLResponse>() {
            @Override
            public void onResponse(XMLResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    if(mCancelItems.size()>0){
                        requestCancelAppo(mCancelItems.remove(0));
                    }else {
                        requestFinishTeam();
                    }
                } else {
                    if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(TeamActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mCancelAppoRequest = new XMLRequest<XMLResponse>(url, listener, errorListener, new XMLResponse());
        mCancelAppoRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mCancelAppoRequest);
    }

    void reqeustSync(TeamScheduleItem item){
        Param param = new Param(ParamUtils.PAGE_APPOINTMENT_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTripItemIndex(item.getTripIndex());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> listener = new Response.Listener<XMLResponse>() {
            @Override
            public void onResponse(XMLResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    if(mSyncItems.size()>0) {
                        reqeustSync(mSyncItems.remove(0));
                    } else {
                        mAllTeamSchduleItems.clear();
                        requestTeamScheduleListAgain(mTeamItem.TeamIndex, ParamUtils.VALUE_FIRST_PAGE_INDEX, true);
                    }
                } else {
                    if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(TeamActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mSyncAppoRequest = new XMLRequest<XMLResponse>(url, listener, errorListener, new XMLResponse());
        mSyncAppoRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mSyncAppoRequest);
    }

    void reqeustSync2(TeamScheduleItem item){
        Param param = new Param(ParamUtils.PAGE_DEAL_INFO_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTripItemIndex(item.getTripIndex());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<XMLResponse> listener = new Response.Listener<XMLResponse>() {
            @Override
            public void onResponse(XMLResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(TeamActivity.this, "同步完成!", Toast.LENGTH_LONG).show();
//                    TeamActivity.this.finish();
                } else {
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeamActivity.this, "同步失败!", Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mSyncAppoRequest = new XMLRequest<XMLResponse>(url, listener, errorListener, new XMLResponse());
        mSyncAppoRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mSyncAppoRequest);
    }


    void requestTeamScheduleList(String teamIndex, Integer page, boolean isRefresh) {
        Param param = new Param(ParamUtils.PAGE_TEAM_SCHEDULE_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTeamIndex(teamIndex);
        param.setPageIndex(page.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<TeamScheduleListResponse> listener = new Response.Listener<TeamScheduleListResponse>() {
            @Override
            public void onResponse(TeamScheduleListResponse response) {
                if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    for (TeamScheduleItem item : response.mItems) {
                        mAllTeamSchduleItems.add(item);
                        if(item.getStatus() == TeamScheduleItem.TRIP_STATUS_BOOK_SUCCESS || item.getStatus() == TeamScheduleItem.TRIP_STATUS_BOOKING){
                            mSyncItems.add(item);
                        }
                    }

                    if (1 == response.mIsLastPage) {
                        if(mSyncItems.size()>0) {
                            reqeustSync(mSyncItems.remove(0));
                        } else {
                            requestFinishTeam();
                        }
                    } else {
                        requestTeamScheduleList(mTeamItem.TeamIndex, response.mPageIndex + 1, false);
                    }
                } else {
                    if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                    if (URLUtils.isDebug) {
                        Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                        Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }
            }
        };

        String url = URLUtils.generateURL(param);
        mTeamScheduleListRequest = new XMLRequest<TeamScheduleListResponse>(url, listener, errorListener, new TeamScheduleListResponse());
        mTeamScheduleListRequest.setShouldCache(true);

        if (isRefresh) {
            /* remove cache before request */
            VolleyRequestQueue.getInstance(this).remove(url);
        }

        VolleyRequestQueue.getInstance(this).add(mTeamScheduleListRequest);
    }


    void requestTeamScheduleListAgain(String teamIndex, Integer page, boolean isRefresh) {
        Param param = new Param(ParamUtils.PAGE_TEAM_SCHEDULE_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTeamIndex(teamIndex);
        param.setPageIndex(page.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        Response.Listener<TeamScheduleListResponse> listener = new Response.Listener<TeamScheduleListResponse>() {
            @Override
            public void onResponse(TeamScheduleListResponse response) {
                if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    for (TeamScheduleItem item : response.mItems) {
                        mAllTeamSchduleItems.add(item);
                        if(item.getStatus() == TeamScheduleItem.TRIP_STATUS_BOOK_SUCCESS || item.getStatus() == TeamScheduleItem.TRIP_STATUS_BOOKING){
                            mCancelItems.add(item);
                        }
                    }

                    if (1 == response.mIsLastPage) {
                        if(mCancelItems.size()>0){
                            requestCancelAppo(mCancelItems.remove(0));
                        } else {
                            requestFinishTeam();
                        }
                    } else {
                        requestTeamScheduleListAgain(mTeamItem.TeamIndex, response.mPageIndex + 1, false);
                    }
                } else {
                    if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(TeamActivity.this, response.mReturnMSG, Toast.LENGTH_LONG).show();
                    if (URLUtils.isDebug) {
                        Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                        Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                }
            }
        };

        String url = URLUtils.generateURL(param);
        mTeamScheduleListRequest = new XMLRequest<TeamScheduleListResponse>(url, listener, errorListener, new TeamScheduleListResponse());
        mTeamScheduleListRequest.setShouldCache(true);

        if (isRefresh) {
            /* remove cache before request */
            VolleyRequestQueue.getInstance(this).remove(url);
        }

        VolleyRequestQueue.getInstance(this).add(mTeamScheduleListRequest);
    }

}
