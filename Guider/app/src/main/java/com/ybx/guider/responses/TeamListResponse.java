package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/4.
 */
public class TeamListResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<TeamItem> mItems;
    TeamItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<TeamItem>();
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
            mItem = new TeamItem();
        } else if (ResponseUtils.TAG_ITEM_TEAM_INDEX.equalsIgnoreCase(TAG)) {
            mItem.TeamIndex = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_AGENCY_NAME.equalsIgnoreCase(TAG)) {
            mItem.AgencyName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_DEPARTMENT_NAME.equalsIgnoreCase(TAG)) {
            mItem.DepartmentName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_START_DATE.equalsIgnoreCase(TAG)) {
            mItem.StartDate = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_END_DATE.equalsIgnoreCase(TAG)) {
            mItem.EndDate = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TOURIST_SOURCE.equalsIgnoreCase(TAG)) {
            mItem.Touristsource = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_ORDER_NUMBER.equalsIgnoreCase(TAG)) {
            mItem.TeamOrderNumber = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_PEOPLE_COUNT_1.equalsIgnoreCase(TAG)) {
            mItem.PepleCount1 = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_PEOPLE_COUNT_2.equalsIgnoreCase(TAG)) {
            mItem.PepleCount2 = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_MEMBER_DESC.equalsIgnoreCase(TAG)) {
            mItem.MemberDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TRIP_DESC.equalsIgnoreCase(TAG)) {
            mItem.TripDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_FROM.equalsIgnoreCase(TAG)) {
            mItem.TeamFrom = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_FROM_DESC.equalsIgnoreCase(TAG)) {
            mItem.TeamFromDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TOTAL_AMOUNT.equalsIgnoreCase(TAG)) {
            mItem.TotalAmount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TOTAL_AMOUNT_DESC.equalsIgnoreCase(TAG)) {
            mItem.TotalAmountDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_CNAME.equalsIgnoreCase(TAG)) {
            mItem.CName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_STATUS.equalsIgnoreCase(TAG)) {
            mItem.Status = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_STATUS_NAME.equalsIgnoreCase(TAG)) {
            mItem.StatusName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_C_DATE_TIME.equalsIgnoreCase(TAG)) {
            mItem.CDateTime = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_MEMO.equalsIgnoreCase(TAG)) {
            mItem.memo = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_IDENTITY_COUNT.equalsIgnoreCase(TAG)) {
            mItem.IdentityCount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TRIP_COUNT.equalsIgnoreCase(TAG)) {
            mItem.TripCount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_DEAL_COUNT.equalsIgnoreCase(TAG)) {
            mItem.DealCount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_LOG_COUNT.equalsIgnoreCase(TAG)) {
            mItem.LogCount = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
