package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ybx.guider.R;

/**
 * Created by chenl on 2016/2/26.
 */
public class DealRecordListAdapter extends BaseAdapter {
    private Context mContext;

    public DealRecordListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 25;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.deal_record_list_item, null);
        }
        return convertView;
    }
}
