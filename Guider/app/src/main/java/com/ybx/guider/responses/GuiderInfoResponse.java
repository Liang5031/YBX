package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class GuiderInfoResponse extends XMLResponse {
    public String GuiderNumber;
    public String Name;
    public String RegistDate;
    public String IdentityNumber;
    public String AddZone1;
    public String Address1;
    public String AddZone2;
    public String Address2;
    public String Education;
    public String Graducation;
    public String Mobile;
    public String Phone;
    public String QQ;
    public String Wechart;
    public String birthday;
    public String Language1;
    public String Language2;
    public String Status;

    private static final String TAG_GUIDERNUMBER = "GuiderNumber";
    private static final String TAG_NAME = "Name";
    private static final String TAG_REGISTDATE = "RegistDate";
    private static final String TAG_IDENTITYNUMBER = "IdentityNumber";
    private static final String TAG_ADDZONE1 = "AddZone1";
    private static final String TAG_ADDRESS1 = "Address1";
    private static final String TAG_ADDZONE2 = "AddZone2";
    private static final String TAG_ADDRESS2= "Address2";
    private static final String TAG_EDUCATION = "Education";
    private static final String TAG_GRADUCATION = "Graducation";
    private static final String TAG_MOBILE = "Mobile";
    private static final String TAG_PHONE = "Phone";
    private static final String TAG_QQ = "QQ";
    private static final String TAG_WECHART = "Wechart";
    private static final String TAG_BIRTHDAY = "Borndate";
    private static final String TAG_LANGUAGE1 = "Language1";
    private static final String TAG_LANGUAGE2 = "Language2";
    private static final String TAG_STATUS = "Status";

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String TAG = parser.getName();

        if (TAG_GUIDERNUMBER.equalsIgnoreCase(TAG)) {
            GuiderNumber = parser.nextText();
        } else if (TAG_NAME.equalsIgnoreCase(TAG)) {
            Name = parser.nextText();
        }else if (TAG_REGISTDATE.equalsIgnoreCase(TAG)) {
            RegistDate = parser.nextText();
        }else if (TAG_IDENTITYNUMBER.equalsIgnoreCase(TAG)) {
            IdentityNumber = parser.nextText();
        }else if (TAG_ADDZONE1.equalsIgnoreCase(TAG)) {
            AddZone1 = parser.nextText();
        }else if (TAG_ADDRESS1.equalsIgnoreCase(TAG)) {
            Address1 = parser.nextText();
        }else if (TAG_ADDZONE2.equalsIgnoreCase(TAG)) {
            AddZone2 = parser.nextText();
        }else if (TAG_ADDRESS2.equalsIgnoreCase(TAG)) {
            Address2 = parser.nextText();
        }else if (TAG_EDUCATION.equalsIgnoreCase(TAG)) {
            Education = parser.nextText();
        }else if (TAG_GRADUCATION.equalsIgnoreCase(TAG)) {
            Graducation = parser.nextText();
        }else if (TAG_MOBILE.equalsIgnoreCase(TAG)) {
            Mobile = parser.nextText();
        }else if (TAG_PHONE.equalsIgnoreCase(TAG)) {
            Phone = parser.nextText();
        }else if (TAG_QQ.equalsIgnoreCase(TAG)) {
            QQ = parser.nextText();
        }else if (TAG_WECHART.equalsIgnoreCase(TAG)) {
            Wechart = parser.nextText();
        }else if (TAG_BIRTHDAY.equalsIgnoreCase(TAG)) {
            birthday = parser.nextText();
        }else if (TAG_LANGUAGE1.equalsIgnoreCase(TAG)) {
            Language1 = parser.nextText();
        }else if (TAG_LANGUAGE2.equalsIgnoreCase(TAG)) {
            Language2 = parser.nextText();
        }else if (TAG_STATUS.equalsIgnoreCase(TAG)) {
            Status = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
    }
}
