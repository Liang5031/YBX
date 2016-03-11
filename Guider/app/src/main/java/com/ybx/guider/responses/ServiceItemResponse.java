package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class ServiceItemResponse extends XMLResponse {

    public ArrayList<ServiceItem> mItems;
    ServiceItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<ServiceItem>();
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String TAG = parser.getName();

        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItem = new ServiceItem();
        } else if (ResponseUtils.TAG_SERVICE_CODE.equalsIgnoreCase(TAG)) {
            mItem.ServiceCode = parser.nextText();
        } else if (ResponseUtils.TAG_SERVICE_NAME.equalsIgnoreCase(TAG)) {
            mItem.ServiceName = parser.nextText();
        }else if (ResponseUtils.TAG_SERVICE_AVAILABLE.equalsIgnoreCase(TAG)) {
            mItem.Useable = parser.nextText();
        }else if (ResponseUtils.TAG_SERVICE_CAPACITY.equalsIgnoreCase(TAG)) {
            mItem.Capacity = parser.nextText();
        }else if (ResponseUtils.TAG_SERVICE_SUB_SECTION.equalsIgnoreCase(TAG)) {
            mItem.IfSubsection = parser.nextText();
        }else if (ResponseUtils.TAG_BOOKING_ACCEPT.equalsIgnoreCase(TAG)) {
            mItem.AppointmentAble = parser.nextText();
        }else if (ResponseUtils.TAG_SERVICE_BOOKING_RATE.equalsIgnoreCase(TAG)) {
            mItem.AppointmentMaxRate = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
