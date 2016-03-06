package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.DealRecordItem;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/26.
 */
public class DealRecordListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DealRecordItem> mItems;
    public DealRecordListAdapter(Context context, ArrayList<DealRecordItem> items) {
        this.mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mItems != null) {
            return mItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void initViews(View view, int position){
        if (mItems != null && mItems.size() > 0) {
            DealRecordItem item = mItems.get(position);
            ((TextView) view.findViewById(R.id.product_name)).setText(item.productName);
            ((TextView) view.findViewById(R.id.dealTime)).setText(item.recDate);
            ((TextView) view.findViewById(R.id.price)).setText(item.price);
            ((TextView) view.findViewById(R.id.count)).setText(item.count);
            ((TextView) view.findViewById(R.id.deal_sum)).setText(item.count);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.deal_record_list_item, null);
            initViews(convertView,position);
        }
        return convertView;
    }
}
