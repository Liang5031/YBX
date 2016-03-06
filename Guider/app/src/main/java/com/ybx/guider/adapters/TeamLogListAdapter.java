package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamLogItem;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/26.
 */
public class TeamLogListAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<TeamLogItem> mItems;

    public TeamLogListAdapter(Context context, ArrayList<TeamLogItem> items) {
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
            TeamLogItem item = mItems.get(position);
            ((TextView) view.findViewById(R.id.dateTime)).setText(ResponseUtils.formatDateTime(item.datetime));
            ((TextView) view.findViewById(R.id.description)).setText(item.description);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.team_log_list_item, null);
            initViews(convertView, position);
        }
        return convertView;
    }
}
