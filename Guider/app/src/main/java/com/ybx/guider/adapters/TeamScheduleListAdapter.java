package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamScheduleItem;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/19.
 */

public class TeamScheduleListAdapter extends BaseAdapter {
    private Context mContext;
    private View mLastView;
    private int mLastPosition;
    private int mLastVisibility;
    private ArrayList<TeamScheduleItem> mAllItems;

    public TeamScheduleListAdapter(Context context, ArrayList<TeamScheduleItem> items) {
        this.mContext = context;
        mLastPosition = -1;
        mAllItems = items;
    }

    @Override
    public int getCount() {
        if (mAllItems != null) {
            return mAllItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mAllItems != null) {
            return mAllItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void initViews(View view, int position) {
        if (mAllItems != null && mAllItems.size() > 0) {
            TeamScheduleItem item = mAllItems.get(position);
            ((TextView) view.findViewById(R.id.schedule_date)).setText(ResponseUtils.formatDate(item.Date));
            ((TextView) view.findViewById(R.id.schedule_TimeRequire)).setText(item.TimeRequire);
            ((TextView) view.findViewById(R.id.schedule_TripType)).setText(item.TripType);
            ((TextView) view.findViewById(R.id.schedule_TranName)).setText(item.TranName);
            ((TextView) view.findViewById(R.id.schedule_Desc)).setText(item.Desc);
            ((TextView) view.findViewById(R.id.schedule_status)).setText(item.Status);
            ((TextView) view.findViewById(R.id.schedule_TripIndex)).setText(item.TripIndex);
            ((TextView) view.findViewById(R.id.schedule_TranType)).setText(item.TranType);
            ((TextView) view.findViewById(R.id.schedule_ProviderNumber)).setText(item.ProviderNumber);
            ((TextView) view.findViewById(R.id.schedule_ProviderName)).setText(item.ProviderName);
            ((TextView) view.findViewById(R.id.schedule_AppoNumber)).setText(item.AppoNumber);
            ((TextView) view.findViewById(R.id.schedule_AppoDesc)).setText(item.AppoDesc);
            ((TextView) view.findViewById(R.id.schedule_ProviderAppMode)).setText(item.ProviderAppMode);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.team_schedule_list_item, null);
            initViews(convertView, position);
            holder = new Holder();
            holder.hideItems = convertView.findViewById(R.id.hideItems);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (mLastPosition == position) {
            holder.hideItems.setVisibility(mLastVisibility);
        } else {
            holder.hideItems.setVisibility(View.GONE);
        }

        return convertView;
    }

    class Holder {
        View hideItems;
    }

    public void changeVisibility(View view, int position) {
        if (mLastView != null && mLastPosition != position) {
            Holder holder = (Holder) mLastView.getTag();
            switch (holder.hideItems.getVisibility()) {
                case View.VISIBLE:
                    holder.hideItems.setVisibility(View.GONE);
                    mLastVisibility = View.GONE;
                    break;
                default:
                    break;
            }
        }

        mLastPosition = position;
        mLastView = view;
        Holder holder = (Holder) view.getTag();

        switch (holder.hideItems.getVisibility()) {
            case View.GONE:
                holder.hideItems.setVisibility(View.VISIBLE);
                mLastVisibility = View.VISIBLE;
                break;

            case View.VISIBLE:
                holder.hideItems.setVisibility(View.GONE);
                mLastVisibility = View.GONE;
                break;
        }
    }
}