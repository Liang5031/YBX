package com.ybx.guider.responses;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.utils.URLUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by chenl on 2016/2/5.
 */
public class XMLResponse {
    public String mReturnCode;
    public String mReturnMSG;
    public String mSign;

    private NetworkResponse mResponse;
    public String rowXML;
//    public void XMLResponse(){}

/*
    public void setResponse(NetworkResponse response) {
        mResponse = response;
    }
*/

    public void parse(NetworkResponse response) throws XmlPullParserException, UnsupportedEncodingException {
        String xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        xmlPullParser.setInput(new StringReader(xmlString));
        try {
            parse(xmlPullParser);
        } catch (IOException e) {
            e.printStackTrace();
            throw new XmlPullParserException(e.getMessage());
        }
    }

    private void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventCode = parser.getEventType();//获取事件类型
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT: //开始读取XML文档
                    startDocument(parser);
                    break;
                case XmlPullParser.START_TAG://开始读取某个标签
                    if (ResponseUtils.RESPONSE_RETURN_CODE.equals(parser.getName())) {
                        setReturnCode(parser.nextText());
                    } else if (ResponseUtils.RESPONSE_RETURN_MSG.equals(parser.getName())) {
                        setReturnMSG(parser.nextText());
                    } else if (ResponseUtils.RESPONSE_SIGN.equals(parser.getName())) {
                        setSign(parser.nextText());
                    } else {
                        startTag(parser);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    endTag(parser);
                    break;
            }
            try {
                eventCode = parser.next();
            } catch (IOException e) {
                e.printStackTrace();
                throw new XmlPullParserException(e.getMessage());
            }
        }
    }

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {

    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {

    }

    public void endTag(XmlPullParser parser) {

    }

    public void setReturnCode(String retcode) {
        mReturnCode = retcode;
    }

    public void setReturnMSG(String retmsg) {
        mReturnMSG = retmsg;
    }

    public void setSign(String sign) {
        mSign = sign;
    }
}
