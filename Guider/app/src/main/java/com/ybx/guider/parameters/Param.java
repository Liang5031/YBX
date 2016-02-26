package com.ybx.guider.parameters;

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


    public Param() {
        setSignType(ParamUtils.VALUE_SIGN_TYPE_MD5);
        setCharSet(ParamUtils.VALUE_DEFAULT_CHAR_SET);
        setServiceVersion(ParamUtils.VALUE_DEFAULT_SERVICE_VERSION);

//        addParam(ParamUtils.KEY_SIGN_TYPE,ParamUtils.VALUE_SIGN_TYPE_MD5);
//        addParam(ParamUtils.KEY_INPUT_CHARSET,ParamUtils.VALUE_DEFAULT_CHAR_SET);
//        addParam(ParamUtils.KEY_SERVICE_VERSION,ParamUtils.VALUE_DEFAULT_SERVICE_VERSION);
//        addParam(ParamUtils.KEY_SIGN,ParamUtils.generateSign() );
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
                return encodedParams.substring(0, encodedParams.length() - 1);
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
                return encodedParams.substring(0, encodedParams.length() - 1);
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
        if (mParams != null && !mParams.containsKey(key)) {
            mParams.put(key, value);
        }
    }

    public void removeParam(String key) {
        if (mParams != null && !mParams.containsKey(key)) {
            mParams.remove(key);
        }
    }

    public String getParam(String key) {
        if (mParams != null && !mParams.containsKey(key)) {
            return mParams.get(key);
        }
        return "";
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

}
