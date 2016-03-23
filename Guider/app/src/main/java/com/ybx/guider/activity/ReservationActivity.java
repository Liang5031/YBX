package com.ybx.guider.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity implements Response.Listener<StartAppointmentResponse>, Response.ErrorListener {
    public static String EXTRA_TEAM_ITEM = "team_item";
    public static String EXTRA_TEAM_SCHEDULE_ITEM = "team_schedule_item";
    public static String EXTRA_RESERVATION_TYPE = "reservation_type";
    public static int TYPE_START = 1;
    public static int TYPE_CHANGE = 2;

    int mReservationType;
    String[] mEmptyList = {"没有可预约时段"};
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

        Intent i = getIntent();
        mReservationType = i.getIntExtra(EXTRA_RESERVATION_TYPE, TYPE_START);
        mTeamItem = (TeamItem) i.getSerializableExtra(EXTRA_TEAM_ITEM);
        mTeamScheduleItem = (TeamScheduleItem) i.getSerializableExtra(EXTRA_TEAM_SCHEDULE_ITEM);

        setTitle("发起预约");
        if (mReservationType == TYPE_CHANGE) {
            setTitle("改签预约");
            ((Button) findViewById(R.id.startAppo)).setText("改签预约");
        }

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
            SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd");
            String today = s.format(new Date());
            mDate.setText(today);
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
                    if (mServiceItems[position].contains(item.ServiceName)) {
                        if (item.Useable.equalsIgnoreCase("1") && item.AppointmentAble.equalsIgnoreCase("1")) {
                            mSelectedService = item.ServiceCode;
                            mProgressDialog = ProgressDialog.show(ReservationActivity.this, "正在加载数据", "请稍等...", true, false);
                            requestTimeSlot(mSelectedService);
                        }
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

        if (mCount == null || mCount.getText().toString().isEmpty()) {
            Toast.makeText(this, "人数不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPhone1 == null || mPhone1.getText().toString().isEmpty()) {
            Toast.makeText(this, "随团手机1不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

        if (mStartAppoRequest != null) {
            mStartAppoRequest.cancel();
        }
    }

    void updateServiceItem() {
        ArrayList serviceItems = new ArrayList();
        for (ServiceItem item : mServiceItemResponse.mItems) {
            if (item.Useable.equalsIgnoreCase("1") && item.AppointmentAble.equalsIgnoreCase("1")) {
                serviceItems.add(item.ServiceName + " - 允许预约");
            } else {
                serviceItems.add(item.ServiceName + " - 禁止预约");
            }
        }

        mServiceItems = (String[]) serviceItems.toArray(new String[serviceItems.size()]);
        mServiceAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mServiceItems);
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
                int remain = 0;
                try {
                    remain = Integer.valueOf(item.Capacity) * Integer.valueOf(item.AppointmentMaxRate) - Integer.valueOf(item.AppointmentCount);
                } catch (NumberFormatException e) {

                }

                if (item.TimeSpanIndex.length() == 1) {
                    item.TimeSpanIndex = "0" + item.TimeSpanIndex;
                }
                String timeSlot = String.format("%s时段: (%s-%s)  余:%d", item.TimeSpanIndex, item.StartTime, item.EndTime, remain);
                timeSlotItems.add(timeSlot);
//                timeSlotItems.add(item.TimeSpanIndex + "时段: （" + item.StartTime + " - " + item.EndTime + " ） 余: " + remain);
            }
        }
        mTimeSlotItems = (String[]) timeSlotItems.toArray(new String[timeSlotItems.size()]);
        mTimeSlotAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mTimeSlotItems);
        mTimeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTimeSlot.setAdapter(mTimeSlotAdapter);
    }

    void requestTimeSlot(String serviceCode) {
        if (serviceCode != null && !serviceCode.isEmpty()) {
            Param param = new Param(ParamUtils.PAGE_SERVICE_TIME_SPAN_QUERY);
            param.setUser(PreferencesUtils.getGuiderNumber(this));
            param.setProviderId(mTeamScheduleItem.getProviderNumber());
            param.setServiceId(serviceCode);
            param.setDate(ResponseUtils.fromDate(mDate.getText().toString()));

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
                        Toast.makeText(ReservationActivity.this, "查询服务时间段失败！", Toast.LENGTH_LONG).show();
                        if (URLUtils.isDebug) {
                            Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                            Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
                        }

                        mTimeSlotAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mEmptyList);
                        mTimeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerTimeSlot.setAdapter(mTimeSlotAdapter);
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.dismiss();
                    Toast.makeText(ReservationActivity.this, "查询服务时间段失败！", Toast.LENGTH_LONG).show();
                    if (URLUtils.isDebug) {
                        Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
                    }

                    mTimeSlotAdapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.spinner_item, mEmptyList);
                    mTimeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinnerTimeSlot.setAdapter(mTimeSlotAdapter);
                }
            };

            mTimeSlotRequest = new XMLRequest<TimeSlotResponse>(url, responseListener, errorListener, new TimeSlotResponse());
            mTimeSlotRequest.setShouldCache(false);

            VolleyRequestQueue.getInstance(this).add(mTimeSlotRequest);
        }
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (year != 0) {
                String newDate = String.format("%04d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);

                SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd");
                String today = s.format(new Date());
                if (newDate.compareTo(today) < 0) {
                    Toast.makeText(ReservationActivity.this, "预约日期不能早于当天！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!newDate.equals(mDate.getText().toString())) {
                    mDate.setText(newDate);
                    requestTimeSlot(mSelectedService);
                }
            }
        }
    };

    public void onClickDate(View view) {
//        MyDatePickerDialog dialog = new MyDatePickerDialog();
//        dialog.show(getSupportFragmentManager(), "datePicker");

        int year = Integer.valueOf(mDate.getText().toString().substring(0, 4));
        int month = Integer.valueOf(mDate.getText().toString().substring(5, 7));
        int day = Integer.valueOf(mDate.getText().toString().substring(8, 10));

        DatePickerDialog dlg = new DatePickerDialog(this, datePickerListener, year, month - 1, day);
        dlg.setTitle("请选择预约日期");
        dlg.show();
    }

    public void onClickStartAppo(View view) {
        if (!inputCheck()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "正在发送预约请求", "请稍等...", true, false);
        requestStartAppointment();
    }


    void requestStartAppointment() {
        String page = ParamUtils.PAGE_START_APPOINTMENT;
        if (mReservationType == TYPE_CHANGE) {
            page = ParamUtils.PAGE_APPOINTMENT_CHANGE;
        }

        Param param = new Param(page);
        param.setUser(PreferencesUtils.getGuiderNumber(this));
        param.setTripItemIndex(mTeamScheduleItem.getTripIndex());
        param.setServiceId(mSelectedService);
        param.setDate(ResponseUtils.fromDate(mDate.getText().toString()));
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

        if (mReservationType == TYPE_CHANGE) {
            param.addParam(ParamUtils.KEY_RESERVATION_NUMBER, mTeamScheduleItem.getAppoNumber());
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
        if (mReservationType == TYPE_CHANGE) {
            Toast.makeText(ReservationActivity.this, "改签失败！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ReservationActivity.this, "预约失败！", Toast.LENGTH_LONG).show();
        }
        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(StartAppointmentResponse response) {
        mProgressDialog.dismiss();
        if (response.mReturnCode.equalsIgnoreCase(ResponseUtils.RESULT_OK)) {
            if (mReservationType == TYPE_CHANGE) {
                Toast.makeText(ReservationActivity.this, "改签成功！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ReservationActivity.this, "预约成功！", Toast.LENGTH_LONG).show();
            }
        } else {
            if (mReservationType == TYPE_CHANGE) {
                Toast.makeText(ReservationActivity.this, "改签失败！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ReservationActivity.this, "预约失败！", Toast.LENGTH_LONG).show();
            }
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }
}
