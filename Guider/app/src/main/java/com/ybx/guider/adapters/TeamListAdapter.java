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

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/26.
 */
public class TeamListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TeamItem> mTeamItems;

    public TeamListAdapter(Context context, ArrayList<TeamItem> teamItems) {
        this.mContext = context;
        mTeamItems = teamItems;
    }

    @Override
    public int getCount() {
        if (mTeamItems != null) {
            return mTeamItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mTeamItems != null) {
            return mTeamItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    String getPeopleCount(int audlt, int child) {
        String FORMAT = "共%d（%d/%d）游客"; /* 共35（30/5）游客 */
        String count = String.format(FORMAT, audlt + child, audlt, child);
        return count;
    }

    void initViews(View view, int position) {
        if (mTeamItems != null && mTeamItems.size() > 0) {
            TeamItem item = mTeamItems.get(position);
            ((TextView) view.findViewById(R.id.team_description)).setText(item.TripDesc);
            ((TextView) view.findViewById(R.id.startEndDate)).setText(ResponseUtils.formatDate(item.StartDate) + " - " + ResponseUtils.formatDate(item.EndDate));
            ((TextView) view.findViewById(R.id.peopleCount)).setText(getPeopleCount(Integer.valueOf(item.PepleCount1), Integer.valueOf(item.PepleCount2)));
            ((TextView) view.findViewById(R.id.totalAmount)).setText("总团款：￥" + item.TotalAmount);
            ((TextView) view.findViewById(R.id.department)).setText(item.AgencyName + " " + item.DepartmentName);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.team_list_item, null);
            initViews(convertView, position);
        }
        return convertView;
    }
}
