package com.ybx.guider.responses;

import com.android.volley.NetworkResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class LoginResponse extends BaseResponse {

    public LoginResponse(NetworkResponse response) throws XmlPullParserException, UnsupportedEncodingException {
        super(response);
    }
}
