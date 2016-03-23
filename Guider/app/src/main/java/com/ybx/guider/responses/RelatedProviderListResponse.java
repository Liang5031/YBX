package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class RelatedProviderListResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<RelatedProviderItem> mItems;
    RelatedProviderItem mItem;

    public static String TAG_PROVIDER_ID = "providerid";
    public static String TAG_PROVIDER_NAME = "providername";
    public static String TAG_STATUS = "status";
    public static String TAG_REQ_DATE = "reqdate";
    public static String TAG_UPDATE_DATE = "upddate";
    public static String TAG_DISABLE_START_DATE = "disstartdate";
    public static String TAG_DISABLE_END_DATE = "disenddate";

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<RelatedProviderItem>();
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
            mItem = new RelatedProviderItem();
        } else if (TAG_PROVIDER_ID.equalsIgnoreCase(TAG)) {
            mItem.providerid = parser.nextText();
        } else if (TAG_PROVIDER_NAME.equalsIgnoreCase(TAG)) {
            mItem.providername = parser.nextText();
        } else if (TAG_STATUS.equalsIgnoreCase(TAG)) {
            mItem.status = parser.nextText();
        } else if (TAG_REQ_DATE.equalsIgnoreCase(TAG)) {
            mItem.reqdate = parser.nextText();
        }else if (TAG_UPDATE_DATE.equalsIgnoreCase(TAG)) {
            mItem.upddate = parser.nextText();
        }else if (TAG_DISABLE_START_DATE.equalsIgnoreCase(TAG)) {
            mItem.disstartdate = parser.nextText();
        }else if (TAG_DISABLE_END_DATE.equalsIgnoreCase(TAG)) {
            mItem.disenddate = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
