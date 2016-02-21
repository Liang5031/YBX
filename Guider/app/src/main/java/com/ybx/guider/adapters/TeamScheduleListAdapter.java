package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ybx.guider.R;

/**
 * Created by chenl on 2016/2/19.
 */

public class TeamScheduleListAdapter extends BaseAdapter {
    private Context mContext;
    private View mLastView;
    private int mLastPosition;
    private int mLastVisibility;

    public TeamScheduleListAdapter(Context context) {
        this.mContext = context;
        mLastPosition = -1;
    }

    @Override
    public int getCount() {
        return 30;
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
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.team_schedule_list_item, null);
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