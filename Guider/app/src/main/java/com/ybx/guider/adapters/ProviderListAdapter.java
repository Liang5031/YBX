package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.DeptItem;
import com.ybx.guider.responses.ProviderItem;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/26.
 */
public class ProviderListAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<ProviderItem> mItems;

    public ProviderListAdapter(Context context) {
        this.mContext = context;
    }

    public void update(ArrayList<ProviderItem> items) {
        mItems = items;
        notifyDataSetChanged();
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
            ProviderItem item = mItems.get(position);
//            ((TextView) view.findViewById(R.id.deptId)).setText(item.customerid);
//            ((TextView) view.findViewById(R.id.deptName)).setText(item.customername);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.dept_list_item, null);
        }
        initViews(convertView, position);
        return convertView;
    }
}
