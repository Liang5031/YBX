package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/15.
 */
public class TeamLogResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<TeamLogItem> mItems;
    TeamLogItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<TeamLogItem>();
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String TAG = parser.getName();

        if (ResponseUtils.TAG_PAGE_COUNT.equalsIgnoreCase(TAG)) {
            mPageCount = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_PAGE_INDEX.equalsIgnoreCase(TAG)) {
            mPageIndex = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_IS_LASTPAGE.equalsIgnoreCase(TAG)) {
            mIsLastPage = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItem = new TeamLogItem();
        } else if(ResponseUtils.TAG_DATE_TIME.equalsIgnoreCase(TAG)){
            mItem.datetime = parser.nextText();
        }else if(ResponseUtils.TAG_LOG_DESC.equalsIgnoreCase(TAG)){
            mItem.description = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
