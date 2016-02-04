package com.ybx.guider.parameters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenl on 2016/2/4.
 */
public class Param {
    public Map<String, String> mParams = new HashMap();
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    public String GetParamString(){
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), getParamsEncoding()));
                encodedParams.append('&');
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getParamsEncoding(), uee);
        }
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public void addParam(String key, String value){
        if(mParams!=null && !mParams.containsKey(key)) {
            mParams.put(key, value);
        }
    }
}
