package com.ybx.guider.parameters;

import android.util.Log;

import com.ybx.guider.utils.URLUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenl on 2016/2/4.
 */
public class Param {
    public Map<String, String> mParams = new HashMap();
    public String mPageName = "";

    public Param(String pageName) {
        mPageName = pageName;
//        setSignType(ParamUtils.VALUE_SIGN_TYPE_MD5);
//        setCharSet(ParamUtils.VALUE_DEFAULT_CHAR_SET);
//        setServiceVersion(ParamUtils.VALUE_DEFAULT_SERVICE_VERSION);
    }

    private void sortByKey() {
        List<Map.Entry<String, String>> paramsList = new ArrayList<Map.Entry<String, String>>(mParams.entrySet());

        Collections.sort(paramsList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
    }

    public String getParamStringInOrder() {
        StringBuilder encodedParams = new StringBuilder();
        List<Map.Entry<String, String>> paramsList = new ArrayList<Map.Entry<String, String>>(mParams.entrySet());

        Collections.sort(paramsList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareToIgnoreCase(o2.getKey());
            }
        });

        try {
            for (Map.Entry<String, String> entry : paramsList) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), getParamsEncoding()));
                encodedParams.append('&');
            }

            if (encodedParams.length() > 0) {
                /* Remove the last '&' */
                String params = encodedParams.substring(0, encodedParams.length() - 1);
                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Ordered parameter string: " + params);
                }
                return params;
            }

            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getParamsEncoding(), uee);
        }
    }

    public String getParamString() {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), getParamsEncoding()));
                encodedParams.append('&');
            }

            if (encodedParams.length() > 0) {
                /* Remove the last '&' */
                String params = encodedParams.substring(0, encodedParams.length() - 1);
                if (URLUtils.isDebug) {
                    Log.d(URLUtils.TAG_DEBUG, "Parameter string: " + params);
                }
                return params;
            }

            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getParamsEncoding(), uee);
        }
    }

    protected String getParamsEncoding() {
        return ParamUtils.VALUE_DEFAULT_CHAR_SET;
    }

    public void addParam(String key, String value) {
        if (mParams != null && !mParams.containsKey(key)
                && value !=null && !value.isEmpty()) {
            mParams.put(key, value);
        }
    }

    public void removeParam(String key) {
        if (mParams != null && mParams.containsKey(key)) {
            mParams.remove(key);
        }
    }

    public String getParam(String key) {
        if (mParams != null && !mParams.containsKey(key)) {
            return mParams.get(key);
        }
        return "";
    }

    public void setPageName(String pageName){
        mPageName = pageName;
    }

    public String getPageName() {
        return mPageName;
    }


    public void setServiceVersion(String version) {
        addParam(ParamUtils.KEY_SERVICE_VERSION, version);
    }

    public void setSignType(String type) {
        addParam(ParamUtils.KEY_SIGN_TYPE, type);
    }

    public void setSign(String sign) {
        addParam(ParamUtils.KEY_SIGN, sign);
    }

    public void setCharSet(String charSet) {
        addParam(ParamUtils.KEY_INPUT_CHARSET, charSet);
    }

    public void setUser(String guiderNumber) {
        addParam(ParamUtils.KEY_USER, guiderNumber);
    }

    public void setProviderId(Integer providerId) {
        addParam(ParamUtils.KEY_PROVIDER_ID, providerId.toString());
    }

    public void setNewPassword(String password) {
        addParam(ParamUtils.KEY_NEW_PASSWORD, password);
    }

    public void setPhoneNumber(String phoneNumber) {
        addParam(ParamUtils.KEY_PHONE_NUMBER, phoneNumber);
    }

    public void setServiceId(String serviceId) {
        addParam(ParamUtils.KEY_SERVICE_ID, serviceId);
    }

    public void setDate(String date) {
        addParam(ParamUtils.KEY_DATE, date);
    }

    public void setTripItemIndex(String tripItemIndex) {
        addParam(ParamUtils.KEY_TRIP_ITEM_INDEX, tripItemIndex);
    }

    public void setTripIndex(String tripIndex) {
        addParam(ParamUtils.KEY_TRIP_INDEX, tripIndex);
    }

    public void setTimeSpanIndex(String timeSpanIndex) {
        addParam(ParamUtils.KEY_TIME_SPAN_INDEX, timeSpanIndex);
    }

    public void setTimeDesc(String timeDesc) {
        addParam(ParamUtils.KEY_TIME_DESC, timeDesc);
    }

    public void setTeamMemberCount(String teamMemberCount) {
        addParam(ParamUtils.KEY_TEAM_MEMBER_COUNT, teamMemberCount);
    }

    public void setTeamMemberDesc(String teamMemberDesc) {
        addParam(ParamUtils.KEY_TEAM_MEMBER_DESC, teamMemberDesc);
    }

    public void setGuiderPhoneNumber1(String guiderPhoneNumber1) {
        addParam(ParamUtils.KEY_GUIDER_PHONE_NUMBER_1, guiderPhoneNumber1);
    }

    public void setGuiderPhoneNumber2(String guiderPhoneNumber2) {
        addParam(ParamUtils.KEY_GUIDER_PHONE_NUMBER_2, guiderPhoneNumber2);
    }

    public void setReservatinoMemo(String reservatinoMemo) {
        addParam(ParamUtils.KEY_RESERVATION_MEMO, reservatinoMemo);
    }

    public void setReservatinoNumber(String reservatinoNumber) {
        addParam(ParamUtils.KEY_RESERVATION_NUMBER, reservatinoNumber);
    }

    public void setBusinessId(String businessId) {
        addParam(ParamUtils.KEY_BUSINESS_ID, businessId);
    }

    public void setGuiderNumber(String guiderNumber) {
        addParam(ParamUtils.KEY_GUIDER_NUMBER, guiderNumber);
    }

    public void setGuiderName(String guiderName) {
        addParam(ParamUtils.KEY_GUIDER_NAME, guiderName);
    }

    public void setGuiderIdentity(String guiderIdentity) {
        addParam(ParamUtils.KEY_GUIDER_IDENTITY_NUMBER, guiderIdentity);
    }

    public void setFistLanguage(String fistLanguage) {
        addParam(ParamUtils.KEY_FIRST_LANGUAGE, fistLanguage);
    }

    public void setSecondLanguage(String secondLanguage) {
        addParam(ParamUtils.KEY_SECOND_LANGUAGE, secondLanguage);
    }

    public void setPassword(String password) {
        addParam(ParamUtils.KEY_PASSWORD, password);
    }

    public void setPageIndex(String pageIndex) {
        addParam(ParamUtils.KEY_PAGE_INDEX, pageIndex);
    }

    public void setTeamIndex(String teamIndex) {
        addParam(ParamUtils.KEY_TEAM_INDEX, teamIndex);
    }

    public void setTeamStatus(String teamStatus) {
        addParam(ParamUtils.KEY_TEAM_STATUS, teamStatus);
    }

    public void setTeamOrderNumber(String teamOrderNumber) {
        addParam(ParamUtils.KEY_TEAM_ORDER_NUMBER, teamOrderNumber);
    }

    public void setAddressZone1(String addressZone1) {
        addParam(ParamUtils.KEY_ADDRESS_ZONE_1, addressZone1);
    }

    public void setAddressZone2(String addressZone2) {
        addParam(ParamUtils.KEY_ADDRESS_ZONE_2, addressZone2);
    }

    public void setAddress1(String address1) {
        addParam(ParamUtils.KEY_ADDRESS_1, address1);
    }

    public void setAddress2(String address2) {
        addParam(ParamUtils.KEY_ADDRESS_2, address2);
    }

    public void setEducation(String education) {
        addParam(ParamUtils.KEY_EDUCATION, education);
    }

    public void setGraduation(String graduation) {
        addParam(ParamUtils.KEY_GRADUATION, graduation);
    }

    public void setPhone(String phone) {
        addParam(ParamUtils.KEY_PHONE, phone);
    }

    public void setQQ(String qq) {
        addParam(ParamUtils.KEY_QQ, qq);
    }

    public void setWeChat(String weChat) {
        addParam(ParamUtils.KEY_WECHAT, weChat);
    }

    public void setVerifyCode(String verifyCode) {
        addParam(ParamUtils.KEY_VERIFY_CODE, verifyCode);
    }

    public void setPhoto(String photo) {
        addParam(ParamUtils.KEY_PHOTO, photo);
    }

}
