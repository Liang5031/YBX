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
 * Created by chenl on 2016/2/5.
 */
public abstract class Response {
    public static String RESPONSE_RETURN_CODE = "retcode";
    public static String RESPONSE_RETURN_MSG = "retmsg";
    public static String RESPONSE_SIGN = "sign";
    public static String RESULT_OK = "0";

    public String mReturnCode;
    public String mReturnMSG;
    public String mSign;

    public Response(NetworkResponse response) throws XmlPullParserException, UnsupportedEncodingException {
        String xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        ;
        xmlPullParser.setInput(new StringReader(xmlString));
        try {
            parse(xmlPullParser, response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new XmlPullParserException(e.getMessage());
        }
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

    private void parse(XmlPullParser parser, NetworkResponse response) throws XmlPullParserException, IOException {

        int eventCode = parser.getEventType();//获取事件类型
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT: //开始读取XML文档
                    startDocument(parser);
                    break;
                case XmlPullParser.START_TAG://开始读取某个标签
                    if (RESPONSE_RETURN_CODE.equals(parser.getName())) {
                        setReturnCode(parser.nextText());
                    } else if (RESPONSE_RETURN_MSG.equals(parser.getName())) {
                        setReturnMSG(parser.nextText());
                    } else if (RESPONSE_SIGN.equals(parser.getName())) {
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
}
