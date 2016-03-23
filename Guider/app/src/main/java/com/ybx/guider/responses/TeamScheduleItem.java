package com.ybx.guider.responses;

import java.io.Serializable;

/**
 * Created by chenl on 2016/3/5.
 */
public class TeamScheduleItem implements Serializable {
    public static final int TRIP_STATUS_INIT = 1;
    public static final int TRIP_STATUS_BOOKING = 2;
    public static final int TRIP_STATUS_BOOK_SUCCESS = 3;
    public static final int TRIP_STATUS_DONE = 4;
    public static final int TRIP_STATUS_BOOKING_CANCEL = 5;
    public static final int TRIP_STATUS_TRIP_CANCEL = 6;
    public static final int TRIP_STATUS_UNKNOW = -1;
    public static final String STATUS_VALUE_INIT = "待执行";
    public static final String STATUS_VALUE_BOOKING = "预约中";
    public static final String STATUS_VALUE_BOOK_SUCCESS = "预约成功";
    public static final String STATUS_VALUE_DONE = "成交";
    public static final String STATUS_VALUE_BOOKING_CANCEL = "预约被撤销";
    public static final String STATUS_VALUE_TRIP_CANCEL = "行程取消";
    public static final String STATUS_VALUE_UNKNOW = "";


    public static final int TRAN_TYPE_EAT = 1;
    public static final int TRAN_TYPE_LIVE = 2;
    public static final int TRAN_TYPE_TRAFFIC = 3;
    public static final int TRAN_TYPE_TOUR = 4;
    public static final int TRAN_TYPE_SHOPPING = 5;
    public static final int TRAN_TYPE_PLAY = 6;
    public static final int TRAN_TYPE_UNKNOW = -1;
    public static final String TRAN_TYPE_VALUE_EAT = "吃";
    public static final String TRAN_TYPE_VALUE_LIVE = "住";
    public static final String TRAN_TYPE_VALUE_TRAFFIC = "行";
    public static final String TRAN_TYPE_VALUE_TOUR = "游";
    public static final String TRAN_TYPE_VALUE_SHOPPING = "购";
    public static final String TRAN_TYPE_VALUE_PLAY = "娱";
    public static final String TRAN_TYPE_VALUE_UNKNOW = "无";


    public static final int TRIP_TYPE_IN_SCHEDULE = 1;
    public static final int TRIP_TYPE_ADDED = 2;
    public static final int TRIP_TYPE_UNKNOW = -1;
    public static final String TRIP_TYPE_VALUE_IN_SCHEDULE = "[行程内]";
    public static final String TRIP_TYPE_VALUE_ADDED = "[加点]";
    public static final String TRIP_TYPE_VALUE_UNKNOW = "[无]";


    public static final int PROVIDER_APP_MODE_NOT_SUPPORT = 0;
    public static final int PROVIDER_APP_MODE_CLOUD = 1;
    public static final int PROVIDER_APP_MODE_REMOTE = 2;
    public static final int PROVIDER_APP_MODE_UNKNOW = -1;
    public static final String PROVIDER_APP_MODE_VALUE_NOT_SUPPORT = "不支持预约";
    public static final String PROVIDER_APP_MODE_VALUE_SUPPORT = "支持预约";
    public static final String PROVIDER_APP_MODE_VALUE_UNKNOW = "";


    int TripType;
    int TranType;
    int Status;
    int ProviderAppMode;
    String Date;
    String TripIndex;
    String TimeRequire;
    String TranName;
    String ProviderNumber;
    String ProviderName;
    String Desc;
    String AppoNumber;
    String AppoDesc;


    public String getDate() {
        return Date;
    }

    public String getTripIndex() {
        return TripIndex;
    }

    public String getTimeRequire() {
        return TimeRequire;
    }

    public int getTripType() {
        return TripType;
    }

    public String getTripTypeValue() {
        switch (TripType) {
            case TRIP_TYPE_IN_SCHEDULE:
                return TRIP_TYPE_VALUE_IN_SCHEDULE;
            case TRIP_TYPE_ADDED:
                return TRIP_TYPE_VALUE_ADDED;
            default:
                return TRIP_TYPE_VALUE_UNKNOW;
        }
    }

    public int getTranType() {
        return TranType;
    }

    public String getTranTypeValue() {
        switch (TranType) {
            case TRAN_TYPE_EAT:
                return TRAN_TYPE_VALUE_EAT;
            case TRAN_TYPE_LIVE:
                return TRAN_TYPE_VALUE_LIVE;
            case TRAN_TYPE_TRAFFIC:
                return TRAN_TYPE_VALUE_TRAFFIC;
            case TRAN_TYPE_TOUR:
                return TRAN_TYPE_VALUE_TOUR;
            case TRAN_TYPE_SHOPPING:
                return TRAN_TYPE_VALUE_SHOPPING;
            case TRAN_TYPE_PLAY:
                return TRAN_TYPE_VALUE_PLAY;
            default:
                return TRAN_TYPE_VALUE_UNKNOW;
        }
    }

    public String getTranName() {
        return TranName;
    }

    public String getProviderNumber() {
        return ProviderNumber;
    }

    public String getProviderName() {
        return ProviderName;
    }

    public String getDesc() {
        return Desc;
    }

    public int getStatus() {
        return Status;
    }

    public String getStatusValue() {
        switch (Status) {
            case TRIP_STATUS_INIT:
                return STATUS_VALUE_INIT;
            case TRIP_STATUS_BOOKING:
                return STATUS_VALUE_BOOKING;
            case TRIP_STATUS_BOOK_SUCCESS:
                return STATUS_VALUE_BOOK_SUCCESS;
            case TRIP_STATUS_DONE:
                return STATUS_VALUE_DONE;
            case TRIP_STATUS_BOOKING_CANCEL:
                return STATUS_VALUE_BOOKING_CANCEL;
            case TRIP_STATUS_TRIP_CANCEL:
                return STATUS_VALUE_TRIP_CANCEL;
            default:
                return STATUS_VALUE_UNKNOW;
        }
    }

    public String getAppoNumber() {
        return AppoNumber;
    }

    public String getAppoDesc() {
        return AppoDesc;
    }

    public int getProviderAppMode() {
        return ProviderAppMode;
    }

    public String getProviderAppModeValue() {
        switch (ProviderAppMode) {
            case PROVIDER_APP_MODE_NOT_SUPPORT:
            case PROVIDER_APP_MODE_CLOUD:
                return PROVIDER_APP_MODE_VALUE_NOT_SUPPORT;

            case PROVIDER_APP_MODE_REMOTE:
                return PROVIDER_APP_MODE_VALUE_SUPPORT;
            default:
                return PROVIDER_APP_MODE_VALUE_UNKNOW;
        }
    }

    public String geTranName() {
        return TranName;
    }
}
