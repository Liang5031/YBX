package com.ybx.guider.responses;

import android.util.Log;

import com.ybx.guider.utils.URLUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class TeamQueryResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<TeamItem> mTeamItems;
    TeamItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mTeamItems = new ArrayList<TeamItem>();
    }

    public void startTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String TAG = parser.getName();

        if (ResponseUtils.TAG_PAGE_COUNT.equals(TAG)) {
            mPageCount = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_PAGE_INDEX.equals(TAG)) {
            mPageIndex = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_IS_LASTPAGE.equals(TAG)) {
            mIsLastPage = Integer.valueOf(parser.nextText());
        } else if (ResponseUtils.TAG_ITEM.equals(TAG)) {
            mItem = new TeamItem();
        } else if (ResponseUtils.TAG_ITEM_TEAM_INDEX.equals(TAG)) {
            mItem.TeamIndex = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_AGENCY_NAME.equals(TAG)) {
            mItem.AgencyName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_DEPARTMENT_NAME.equals(TAG)) {
            mItem.DepartmentName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_START_DATE.equals(TAG)) {
            mItem.StartDate = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_END_DATE.equals(TAG)) {
            mItem.EndDate = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TOURIST_SOURCE.equals(TAG)) {
            mItem.Touristsource = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_ORDER_NUMBER.equals(TAG)) {
            mItem.TeamOrderNumber = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_PEOPLE_COUNT_1.equals(TAG)) {
            mItem.PepleCount1 = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_PEOPLE_COUNT_2.equals(TAG)) {
            mItem.PepleCount2 = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_MEMBER_DESC.equals(TAG)) {
            mItem.MemberDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TRIP_DESC.equals(TAG)) {
            mItem.TripDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_FROM.equals(TAG)) {
            mItem.TeamFrom = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_FROM_DESC.equals(TAG)) {
            mItem.TeamFromDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TOTAL_AMOUNT.equals(TAG)) {
            mItem.TotalAmount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TOTAL_AMOUNT_DESC.equals(TAG)) {
            mItem.TotalAmountDesc = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_CNAME.equals(TAG)) {
            mItem.CName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_STATUS.equals(TAG)) {
            mItem.Status = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_STATUS_NAME.equals(TAG)) {
            mItem.StatusName = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_C_DATE_TIME.equals(TAG)) {
            mItem.CDateTime = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TEAM_MEMO.equals(TAG)) {
            mItem.memo = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_IDENTITY_COUNT.equals(TAG)) {
            mItem.IdentityCount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_TRIP_COUNT.equals(TAG)) {
            mItem.TripCount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_DEAL_COUNT.equals(TAG)) {
            mItem.DealCount = parser.nextText();
        } else if (ResponseUtils.TAG_ITEM_LOG_COUNT.equals(TAG)) {
            mItem.LogCount = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equals(TAG)) {
            mTeamItems.add(mItem);
        }
    }
}
