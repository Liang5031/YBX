package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class BindingDeptListResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<BindingDeptItem> mItems;
    BindingDeptItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<BindingDeptItem>();
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
            mItem = new BindingDeptItem();
        } else if (ResponseUtils.TAG_CUSTOMER_ID.equalsIgnoreCase(TAG)) {
            mItem.customerid = parser.nextText();
        } else if (ResponseUtils.TAG_CUSTOMER_TYPE.equalsIgnoreCase(TAG)) {
            mItem.customertype = parser.nextText();
        } else if (ResponseUtils.TAG_CUSTOMER_NAME.equalsIgnoreCase(TAG)) {
            mItem.customername = parser.nextText();
        } else if (ResponseUtils.TAG_YYXCXK.equalsIgnoreCase(TAG)) {
            mItem.YYXCXK = parser.nextText();
        } else if (ResponseUtils.TAG_WPTDXK.equalsIgnoreCase(TAG)) {
            mItem.WPTDXK = parser.nextText();
        } else if (ResponseUtils.TAG_CJTDXK.equalsIgnoreCase(TAG)) {
            mItem.CJTDXK = parser.nextText();
        } else if (ResponseUtils.TAG_RQSJ.equalsIgnoreCase(TAG)) {
            mItem.RQSJ = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
