package com.ybx.guider.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.ServiceItem;
import com.ybx.guider.responses.ServiceItemResponse;
import com.ybx.guider.responses.StartAppointmentResponse;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamScheduleItem;
import com.ybx.guider.responses.TimeSlotItem;
import com.ybx.guider.responses.TimeSlotResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity implements Response.Listener<StartAppointmentResponse>, Response.ErrorListener {
    public static String EXTRA_TEAM_ITEM = "team_item";
    public static String EXTRA_TEAM_SCHEDULE_ITEM = "team_schedule_item";
    String[] mEmptyList = {"无"};
    private Spinner mSpinnerServices;
    private Spinner mSpinnerTimeSlot;
    private ArrayAdapter<String> mServiceAdapter;
    private ArrayAdapter<String> mTimeSlotAdapter;
    private ProgressDialog mProgressDialog;
    TeamScheduleItem mTeamScheduleItem;
    TeamItem mTeamItem;
    ServiceItemResponse mServiceItemResponse;
    TimeSlotResponse mTimeSlotResponse;
    String[] mServiceItems;
    String[] mTimeSlotItems;
    String mSelectedService;
    String mSelectedTimeSlot;
    EditText mDate;
    EditText mCount;
    EditText mMemberDesc;
    EditText mPhone1;
    EditText mPhone2;
    EditText mMemo;

    XMLRequest<ServiceItemResponse> mServiceItemRequest;
    XMLRequest<TimeSlotResponse> mTimeSlotRequest;
    XMLRequest<StartAppointmentResponse> mStartAppoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        setTitle("发起预约");
        Intent i = getIntent();
        mTeamItem = (TeamItem) i.getSerializableExtra(EXTRA_TEAM_ITEM);
        mTeamScheduleItem = (TeamScheduleItem) i.getSerializableExtra(EXTRA_TEAM_SCHEDULE_ITEM);

        ((TextView) findViewById(R.id.schedule_TranType)).setText(mTeamScheduleItem.getTranTypeValue());
        ((TextView) findViewById(R.id.schedule_date)).setText(ResponseUtils.formatDate(mTeamScheduleItem.getDate()));
        ((TextView) findViewById(R.id.schedule_TimeRequire)).setText(mTeamScheduleItem.getTimeRequire());
        ((TextView) findViewById(R.id.schedule_TranName)).setText(mTeamScheduleItem.geTranName());
        ((TextView) findViewById(R.id.schedule_TripType)).setText(mTeamScheduleItem.getTripTypeValue());
        ((TextView) findViewById(R.id.schedule_ProviderAppMode)).setText(mTeamScheduleItem.getProviderAppModeValue());
        ((TextView) findViewById(R.id.schedule_Desc)).setText(mTeamScheduleItem.getDesc());
        ((TextView) findViewById(R.id.appoDesc)).setText(mTeamScheduleItem.getAppoDesc());


        mDate = (EditText) findViewById(R.id.date);
        mCount = (EditText) findViewById(R.id.peopleCount);
        mMemberDesc = (EditText) findViewById(R.id.memberDesc);
        mPhone1 = (EditText) findViewById(R.id.phone1);
        mPhone2 = (EditText) findViewById(R.id.phone2);
        mMemo = (EditText) findViewById(R.id.memo);

        if (mTeamScheduleItem != null && mTeamItem != null) {
            mDate.setText(ResponseUtils.formatDate(mTeamScheduleItem.getDate()));
            Integer count = Integer.valueOf(mTeamItem.PepleCount1) + Integer.valueOf(mTeamItem.PepleCount2);
            mCount.setText(count.toString());
            mMemberDesc.setText(mTeamItem.MemberDesc);
        }

        mSpinnerServices = (Spinner) findViewById(R.id.spinnerServices);
        mServiceAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mEmptyList);
        mServiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerServices.setAdapter(mServiceAdapter);

        mSpinnerTimeSlot = (Spinner) findViewById(R.id.spinnerTimeSlot);
        mTimeSlotAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mEmptyList);
        mTimeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTimeSlot.setAdapter(mTimeSlotAdapter);

        //添加事件Spinner事件监听
        mSpinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mServiceItemResponse == null)
                    return;

                for (ServiceItem item : mServiceItemResponse.mItems) {
                    if (mServiceItems[position].equalsIgnoreCase(item.ServiceName)) {
                        mSelectedService = item.ServiceCode;
                        mProgressDialog = ProgressDialog.show(ReservationActivity.this, "正在加载数据", "请稍等...", true, false);
                        requestTimeSlot(mSelectedService);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mTimeSlotResponse == null)
                    return;

                for (TimeSlotItem item : mTimeSlotResponse.mItems) {
                    if (mTimeSlotItems[position].contains(item.StartTime) && mTimeSlotItems[position].contains(item.EndTime)) {
                        mSelectedTimeSlot = item.TimeSpanIndex;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置默认值
//        mSpinnerServices.setVisibility(View.VISIBLE);
//        mSpinnerTimeSlot.setVisibility(View.VISIBLE);

        mProgressDialog = ProgressDialog.show(this, "正在加载数据", "请稍等...", true, false);
        requestServiceItem();
    }

    boolean inputCheck() {
        if (mDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "预约日期不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mSelectedService == null || mSelectedService.toString().isEmpty()) {
            Toast.makeText(this, "请选择需要预约的服务！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mSelectedTimeSlot == null || mSelectedTimeSlot.toString().isEmpty()) {
            Toast.makeText(this, "请选择预约的时间段！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mCount == null || mCount.toString().isEmpty()) {
            Toast.makeText(this, "人数不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPhone1 == null || mPhone1.toString().isEmpty()) {
            Toast.makeText(this, "随团手机1不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                Intent intent = new Intent(this, TeamActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(TeamActivity.EXTRA_TEAM_ITEM, mTeamItem);
//                intent.putExtras(bundle);
//                NavUtils.navigateUpTo(this, intent);
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceItemRequest != null) {
            mServiceItemRequest.cancel();
        }

        if (mTimeSlotRequest != null) {
            mTimeSlotRequest.cancel();
        }
    }

    void updateServiceItem() {
        ArrayList<String> serviceItems = new ArrayList<String>();
        for (ServiceItem item : mServiceItemResponse.mItems) {
            if (item.Useable.equalsIgnoreCase("1") && item.AppointmentAble.equalsIgnoreCase("1")) {
                serviceItems.add(item.ServiceName);
            }
        }
        mServiceItems = (String[]) serviceItems.toArray();
        mServiceAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mServiceItems);
//        mServiceAdapter = new ArrayAdapter<String>(ReservationActivity.this,R.layout.spinner_item,new String[]{"item1","item2","item3"});
        mServiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerServices.setAdapter(mServiceAdapter);
    }

    void requestServiceItem() {
        Param param = new Param(ParamUtils.PAGE_SERVICE_ITEM_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setProviderId(mTeamScheduleItem.getProviderNumber());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        Response.Listener<ServiceItemResponse> responseListener = new Response.Listener<ServiceItemResponse>() {

            @Override
            public void onResponse(ServiceItemResponse response) {
                mProgressDialog.dismiss();
                if (response.mReturnCode.equalsIgnoreCase(ResponseUtils.RESULT_OK)) {
                    mServiceItemResponse = response;
                    updateServiceItem();
                } else {
                    Toast.makeText(ReservationActivity.this, "查询服务项目失败！", Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ReservationActivity.this, "查询服务项目失败！", Toast.LENGTH_LONG).show();
            }
        };

        mServiceItemRequest = new XMLRequest<ServiceItemResponse>(url, responseListener, errorListener, new ServiceItemResponse());
        mServiceItemRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mServiceItemRequest);
    }


    void updateTimeSlotItem() {
        ArrayList<String> timeSlotItems = new ArrayList<String>();
        for (TimeSlotItem item : mTimeSlotResponse.mItems) {
            if (item.AppointmentAble.equalsIgnoreCase("1")) {
                timeSlotItems.add(item.StartTime + " - " + item.EndTime);
            }
        }
        mTimeSlotItems = (String[]) timeSlotItems.toArray();
        mTimeSlotAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mTimeSlotItems);
//        mTimeSlotAdapter = new ArrayAdapter<String>(ReservationActivity.this,R.layout.spinner_item,new String[]{"item1","item2","item3"});
        mTimeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTimeSlot.setAdapter(mTimeSlotAdapter);
    }

    void requestTimeSlot(String serviceCode) {
        Param param = new Param(ParamUtils.PAGE_SERVICE_TIME_SPAN_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setProviderId(mTeamScheduleItem.getProviderNumber());
        param.setServiceId(serviceCode);
        param.setDate(mDate.getText().toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);

        Response.Listener<TimeSlotResponse> responseListener = new Response.Listener<TimeSlotResponse>() {

            @Override
            public void onResponse(TimeSlotResponse response) {
                mProgressDialog.dismiss();
                if (response.mReturnCode.equalsIgnoreCase(ResponseUtils.RESULT_OK)) {
                    mTimeSlotResponse = response;
                    updateTimeSlotItem();
                } else {
                    Toast.makeText(ReservationActivity.this, "查询服务项目失败！", Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ReservationActivity.this, "查询服务时间段失败！", Toast.LENGTH_LONG).show();
            }
        };

        mTimeSlotRequest = new XMLRequest<TimeSlotResponse>(url, responseListener, errorListener, new TimeSlotResponse());
        mTimeSlotRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mTimeSlotRequest);
    }

    public void onClickDate(View view){

    }

    public void onClickStartAppo(View view) {
        if (!inputCheck()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "正在发送预约请求", "请稍等...", true, false);
        requestStartAppointment();
    }


    void requestStartAppointment() {
        Param param = new Param(ParamUtils.PAGE_START_APPOINTMENT);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTripItemIndex(mTeamScheduleItem.getTripIndex());
        param.setServiceId(mSelectedService);
        param.setDate(mDate.getText().toString());
        param.setTimeSpanIndex(mSelectedTimeSlot);
        param.setTeamMemberCount(mCount.getText().toString());

        if (!mMemberDesc.getText().toString().isEmpty()) {
            param.setTeamMemberDesc(mMemberDesc.getText().toString());
        }

        param.setGuiderPhoneNumber1(mPhone1.getText().toString());

        if (!mPhone2.getText().toString().isEmpty()) {
            param.setGuiderPhoneNumber2(mPhone2.getText().toString());
        }

        if (!mMemo.getText().toString().isEmpty()) {
            param.setReservatinoMemo(mMemo.getText().toString());
        }


        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mStartAppoRequest = new XMLRequest<StartAppointmentResponse>(url, this, this, new StartAppointmentResponse());
        mStartAppoRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this).add(mStartAppoRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
        Toast.makeText(ReservationActivity.this, "预约失败！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(StartAppointmentResponse response) {
        mProgressDialog.dismiss();
        if (response.mReturnCode.equalsIgnoreCase(ResponseUtils.RESULT_OK)) {
            Toast.makeText(ReservationActivity.this, "预约成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ReservationActivity.this, "查询服务项目失败！", Toast.LENGTH_LONG).show();
        }
    }
}
