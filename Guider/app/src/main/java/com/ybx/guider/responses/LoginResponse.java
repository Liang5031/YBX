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

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (ResponseUtils.TAG_ACCOUNT_STATUS.equals(parser.getName())) {
            mAccountStatus = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
    }
}
