package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/4.
 */
public class TimeSlotResponse extends XMLResponse {

    public ArrayList<TimeSlotItem> mItems;
    TimeSlotItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<TimeSlotItem>();
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String TAG = parser.getName();

        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItem = new TimeSlotItem();
        } else if (ResponseUtils.TAG_SERVICE_CODE.equalsIgnoreCase(TAG)) {
            mItem.ServiceCode = parser.nextText();
        } else if (ResponseUtils.TAG_SERVICE_DATE.equalsIgnoreCase(TAG)) {
            mItem.Date = parser.nextText();
        } else if (ResponseUtils.TAG_TIME_SPAN_INDEX.equalsIgnoreCase(TAG)) {
            mItem.TimeSpanIndex = parser.nextText();
        } else if (ResponseUtils.TAG_START_TIME.equalsIgnoreCase(TAG)) {
            mItem.StartTime = parser.nextText();
        } else if (ResponseUtils.TAG_END_TIME.equalsIgnoreCase(TAG)) {
            mItem.EndTime = parser.nextText();
        } else if (ResponseUtils.TAG_SERVICE_CAPACITY.equalsIgnoreCase(TAG)) {
            mItem.Capacity = parser.nextText();
        } else if (ResponseUtils.TAG_BOOKING_ACCEPT.equalsIgnoreCase(TAG)) {
            mItem.AppointmentAble = parser.nextText();
        } else if (ResponseUtils.TAG_SERVICE_BOOKING_RATE.equalsIgnoreCase(TAG)) {
            mItem.AppointmentMaxRate = parser.nextText();
        } else if (ResponseUtils.TAG_APPO_COUNT.equalsIgnoreCase(TAG)) {
            mItem.AppointmentCount = parser.nextText();
        } else if (ResponseUtils.TAG_APPO_USER_COUNT.equalsIgnoreCase(TAG)) {
            mItem.AppointmentUseCount = parser.nextText();
        } else if (ResponseUtils.TAG_OTHER_USER_COUNT.equalsIgnoreCase(TAG)) {
            mItem.OtherUseCount = parser.nextText();
        } else if (ResponseUtils.TAG_APPO_AVAILABLE.equalsIgnoreCase(TAG)) {
            mItem.UseToAppointmentAble = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
