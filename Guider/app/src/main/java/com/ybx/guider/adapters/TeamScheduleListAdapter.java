package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
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
    TeamItem mTeamItem;

    public TeamScheduleListAdapter(Context context, TeamItem item) {
        this.mContext = context;
        mLastPosition = -1;
        mTeamItem = item;
    }

    public void updateData(ArrayList<TeamScheduleItem> items){
        mAllItems = items;
        notifyDataSetChanged();
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

    void hideAllButton(View view) {
        view.findViewById(R.id.btnFinish).setVisibility(View.GONE);
        view.findViewById(R.id.btnCancelSchedule).setVisibility(View.GONE);
        view.findViewById(R.id.btnStartAppo).setVisibility(View.GONE);
        view.findViewById(R.id.btnCancelAppo).setVisibility(View.GONE);
        view.findViewById(R.id.btnChangeAppo).setVisibility(View.GONE);
        view.findViewById(R.id.btnSync).setVisibility(View.GONE);
    }

    void setButtonVisibility(TeamScheduleItem item, View view) {
        hideAllButton(view);

        if(!mTeamItem.Status.equals("4")){
            return;
        }

        int status = item.getStatus();

        switch (status) {
            case TeamScheduleItem.TRIP_STATUS_INIT:
                if (TeamScheduleItem.PROVIDER_APP_MODE_REMOTE == item.getProviderAppMode()) {
                    view.findViewById(R.id.btnStartAppo).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.btnStartAppo).setTag(item);
                }
                break;

            case TeamScheduleItem.TRIP_STATUS_BOOKING:/* fall through */
            case TeamScheduleItem.TRIP_STATUS_BOOK_SUCCESS:
                view.findViewById(R.id.btnCancelAppo).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btnCancelAppo).setTag(item);
                view.findViewById(R.id.btnChangeAppo).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btnChangeAppo).setTag(item);
                view.findViewById(R.id.btnSync).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btnSync).setTag(item);
                break;

            case TeamScheduleItem.TRIP_STATUS_BOOKING_CANCEL:
                if (TeamScheduleItem.PROVIDER_APP_MODE_REMOTE == item.getProviderAppMode()) {
                    view.findViewById(R.id.btnStartAppo).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.btnStartAppo).setTag(item);
                }
                break;

//            case TeamScheduleItem.TRIP_STATUS_DONE:/* fall through */
//            case TeamScheduleItem.TRIP_STATUS_TRIP_CANCEL:
//                view.findViewById(R.id.btnFinish).setVisibility(View.VISIBLE);
//                view.findViewById(R.id.btnFinish).setTag(item);
//                view.findViewById(R.id.btnCancelSchedule).setVisibility(View.VISIBLE);
//                view.findViewById(R.id.btnCancelSchedule).setTag(item);
//                break;
            default:
                break;
        }
    }

    void initViews(View view, int position) {
        if (mAllItems != null && mAllItems.size() > 0) {
            TeamScheduleItem item = mAllItems.get(position);
            ((TextView) view.findViewById(R.id.schedule_TranType)).setText(item.getTranTypeValue());
            ((TextView) view.findViewById(R.id.schedule_date)).setText(ResponseUtils.formatDate(item.getDate()));
            ((TextView) view.findViewById(R.id.schedule_TimeRequire)).setText(item.getTimeRequire());
            ((TextView) view.findViewById(R.id.schedule_TranName)).setText(item.geTranName());
            ((TextView) view.findViewById(R.id.schedule_TripType)).setText(item.getTripTypeValue());
            ((TextView) view.findViewById(R.id.schedule_ProviderAppMode)).setText(item.getProviderAppModeValue());
            ((TextView) view.findViewById(R.id.schedule_Desc)).setText(item.getDesc());
            ((TextView) view.findViewById(R.id.schedule_TimeRequire2)).setText(item.getTimeRequire());
            ((TextView) view.findViewById(R.id.schedule_TranName2)).setText(item.geTranName());
            ((TextView) view.findViewById(R.id.schedule_Desc2)).setText(item.getDesc());
            ((TextView) view.findViewById(R.id.schedule_ProviderName)).setText(item.getProviderName());
            ((TextView) view.findViewById(R.id.schedule_status)).setText(item.getStatusValue());
            ((TextView) view.findViewById(R.id.schedule_AppoNumber)).setText(item.getAppoNumber());
            ((TextView) view.findViewById(R.id.schedule_AppoDesc)).setText(item.getAppoDesc());
//            ((TextView) view.findViewById(R.id.schedule_TripIndex)).setText(item.getTripIndex());

            setButtonVisibility(item, view);
        }
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
        initViews(convertView, position);

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