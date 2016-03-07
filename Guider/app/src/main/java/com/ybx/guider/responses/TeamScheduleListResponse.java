package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenl on 2016/3/5.
 */
public class TeamScheduleListResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<TeamScheduleItem> mItems;
    TeamScheduleItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<TeamScheduleItem>();
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String TAG = parser.getName();

        if (ResponseUtils.TAG_PAGE_COUNT.equalsIgnoreCase(TAG)) {
            mPageCount = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_PAGE_INDEX.equalsIgnoreCase(TAG)) {
            mPageIndex = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_IS_LASTPAGE.equalsIgnoreCase(TAG)) {
            mIsLastPage = Integer.valueOf(parser.nextText());
        }else if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItem = new TeamScheduleItem();
        }else if (ResponseUtils.TAG_DATE.equalsIgnoreCase(TAG)) {
            mItem.Date = parser.nextText();
        }else if (ResponseUtils.TAG_TRIP_INDEX.equalsIgnoreCase(TAG)) {
            mItem.TripIndex = parser.nextText();;
        }else if (ResponseUtils.TAG_TIME_REQUIRE.equalsIgnoreCase(TAG)) {
            mItem.TimeRequire = parser.nextText();;
        }else if (ResponseUtils.TAG_TRIP_TYPE.equalsIgnoreCase(TAG)) {
            try {
                mItem.TripType = Integer.valueOf(parser.nextText());
            } catch(NumberFormatException e){
                mItem.TripType = TeamScheduleItem.TRIP_TYPE_UNKNOW;
            }
        }else if (ResponseUtils.TAG_TRAN_TYPE.equalsIgnoreCase(TAG)) {
            try {
                mItem.TranType = Integer.valueOf(parser.nextText());
            } catch(NumberFormatException e){
                mItem.TranType = TeamScheduleItem.TRAN_TYPE_UNKNOW;
            }
        }else if (ResponseUtils.TAG_TRAN_NAME.equalsIgnoreCase(TAG)) {
            mItem.TranName = parser.nextText();;
        }else if (ResponseUtils.TAG_PROVIDER_NUMBER.equalsIgnoreCase(TAG)) {
            mItem.ProviderNumber = parser.nextText();;
        }else if (ResponseUtils.TAG_PROVIDER_NAME.equalsIgnoreCase(TAG)) {
            mItem.ProviderName = parser.nextText();;
        }else if (ResponseUtils.TAG_DESC.equalsIgnoreCase(TAG)) {
            mItem.Desc = parser.nextText();;
        }else if (ResponseUtils.TAG_STATUS.equalsIgnoreCase(TAG)) {
            try {
                mItem.Status = Integer.valueOf(parser.nextText());
            } catch(NumberFormatException e){
                mItem.Status = TeamScheduleItem.TRIP_STATUS_UNKNOW;
            }
        }else if (ResponseUtils.TAG_APPO_NUMBER.equalsIgnoreCase(TAG)) {
            mItem.AppoNumber = parser.nextText();;
        }else if (ResponseUtils.TAG_APPO_DESC.equalsIgnoreCase(TAG)) {
            mItem.AppoDesc = parser.nextText();;
        }else if (ResponseUtils.TAG_PROVIDER_APP_MODE.equalsIgnoreCase(TAG)) {
            try {
                mItem.ProviderAppMode = Integer.valueOf(parser.nextText());
            } catch(NumberFormatException e){
                mItem.ProviderAppMode = TeamScheduleItem.PROVIDER_APP_MODE_UNKNOW;
            }
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
