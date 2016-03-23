package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class ProviderListResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<ProviderItem> mItems;
    ProviderItem mItem;

    public static String TAG_PROVIDER_ID = "providerid";
    public static String TAG_PROVIDER_NAME = "providername";
    public static String TAG_PROVIDER_TYPE = "Type";
    public static String TAG_PROVIDER_APPOINTMENTABLE = "appointmentable";


    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<ProviderItem>();
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
            mItem = new ProviderItem();
        } else if (TAG_PROVIDER_ID.equalsIgnoreCase(TAG)) {
            mItem.providerid = parser.nextText();
        } else if (TAG_PROVIDER_NAME.equalsIgnoreCase(TAG)) {
            mItem.providername = parser.nextText();
        } else if (TAG_PROVIDER_TYPE.equalsIgnoreCase(TAG)) {
            mItem.type = parser.nextText();
        } else if (TAG_PROVIDER_APPOINTMENTABLE.equalsIgnoreCase(TAG)) {
            mItem.appointmentable = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
