package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.RealNameItem;
import com.ybx.guider.responses.ResponseUtils;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/26.
 */
public class RealNameListAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<RealNameItem> mItems;

    public RealNameListAdapter(Context context, ArrayList<RealNameItem> items) {
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

    void initViews(View view, int position) {
        if (mItems != null && mItems.size() > 0) {
            RealNameItem item = mItems.get(position);
//            ((TextView) view.findViewById(R.id.realNameIdx)).setText(position);
            ((TextView) view.findViewById(R.id.number)).setText(item.Number);
            ((TextView) view.findViewById(R.id.name)).setText(item.Name);
            ((TextView) view.findViewById(R.id.type)).setText(ResponseUtils.getIdentidyType(item.Type));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.real_name_list_item, null);
            initViews(convertView, position);
        }
        return convertView;
    }
}
