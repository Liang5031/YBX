package com.ybx.guider.responses;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class LoginResponse extends Response {

    public LoginResponse(NetworkResponse response) throws XmlPullParserException, UnsupportedEncodingException {
        super(response);
    }

    @Override
    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {

    }

    @Override
    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {

    }

    @Override
    public void endTag(XmlPullParser parser) {

    }
}
