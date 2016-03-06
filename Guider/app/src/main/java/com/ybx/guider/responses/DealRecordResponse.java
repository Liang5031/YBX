package com.ybx.guider.responses;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/15.
 */
public class DealRecordResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<DealRecordItem> mItems;
    DealRecordItem mItem;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<DealRecordItem>();
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
            mItem = new DealRecordItem();
        } else if(ResponseUtils.TAG_REC_NUMBER.equalsIgnoreCase(TAG)){
            mItem.recordNumber = parser.nextText();
        }else if(ResponseUtils.TAG_PROVIDER_ID.equalsIgnoreCase(TAG)){
            mItem.providerId = parser.nextText();
        }else if(ResponseUtils.TAG_BILL_NUMBER.equalsIgnoreCase(TAG)){
            mItem.billNumber = parser.nextText();
        }else if(ResponseUtils.TAG_APPO_NUM.equalsIgnoreCase(TAG)){
            mItem.appointmentNum = parser.nextText();
        }else if(ResponseUtils.TAG_PRODUCT_NAME.equalsIgnoreCase(TAG)){
            mItem.productName = parser.nextText();
        }else if(ResponseUtils.TAG_PRODUCT_ID.equalsIgnoreCase(TAG)){
            mItem.productId = parser.nextText();
        }else if(ResponseUtils.TAG_PRICE.equalsIgnoreCase(TAG)){
            mItem.price = parser.nextText();
        }else if(ResponseUtils.TAG_COUNT.equalsIgnoreCase(TAG)){
            mItem.count = parser.nextText();
        }else if(ResponseUtils.TAG_REC_DATE.equalsIgnoreCase(TAG)){
            mItem.recDate = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItems.add(mItem);
        }
    }
}
