package com.ybx.guider.responses;

import android.util.Log;

import com.ybx.guider.utils.URLUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/15.
 */
public class RealNameInfoResponse extends XMLResponse {
    public int mPageCount;
    public int mPageIndex;
    public int mIsLastPage;
    public ArrayList<RealNameItem> mItems;
    RealNameItem mItem;
    int index = 1;

    public void startDocument(XmlPullParser parser) throws IOException, XmlPullParserException {
        mItems = new ArrayList<RealNameItem>();
        index = 1;
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
            mItem = new RealNameItem();
        } else if(ResponseUtils.TAG_NUMBER.equalsIgnoreCase(TAG)){
            mItem.Number = parser.nextText();
        }else if(ResponseUtils.TAG_NAME.equalsIgnoreCase(TAG)){
            mItem.Name = parser.nextText();
        }else if(ResponseUtils.TAG_TYPE.equalsIgnoreCase(TAG)){
            mItem.Type = parser.nextText();
        }
    }

    public void endTag(XmlPullParser parser) {
        String TAG = parser.getName();
        if (ResponseUtils.TAG_ITEM.equalsIgnoreCase(TAG)) {
            mItem.Index = index++;
            mItems.add(mItem);
        }
    }
}
