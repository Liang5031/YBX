package com.ybx.guider.responses;

import com.android.volley.NetworkResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class LoginResponse {

    public LoginResponse(NetworkResponse response) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = factory.newPullParser();
        parse(xmlPullParser, response);
    }

    private void parse(XmlPullParser parser, NetworkResponse response){

    }

}
