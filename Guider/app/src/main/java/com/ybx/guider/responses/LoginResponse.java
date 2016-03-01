package com.ybx.guider.responses;

import android.util.Log;

import com.ybx.guider.utils.URLUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class LoginResponse extends XMLResponse {
    public String mAccountStatus;

//    public LoginResponse(NetworkResponse response) throws XmlPullParserException, UnsupportedEncodingException {
//        super(response);
//    }

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(URLUtils.TAG_DEBUG, "startDocument called");
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (ResponseUtils.RESPONSE_ACCOUNT_STATUS.equals(parser.getName())) {
            mAccountStatus = parser.nextText();
        }
        Log.d(URLUtils.TAG_DEBUG, "startTag called");
    }

    public void endTag(XmlPullParser parser) {
        Log.d(URLUtils.TAG_DEBUG, "endTag called");
    }
}
