package com.ybx.guider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamQueryResponse;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/26.
 */
public class TeamListAdapter extends BaseAdapter {
    private Context mContext;
    private TeamQueryResponse mResponse;
    private ArrayList<TeamItem> mTeamItems;

    public TeamListAdapter(Context context, ArrayList<TeamItem> teamItems) {
        this.mContext = context;
        mTeamItems = teamItems;
    }

    @Override
    public int getCount() {
        if( mResponse !=null && mResponse.mTeamItems !=null ){
            return mResponse.mTeamItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if( mTeamItems !=null ){
            return mTeamItems.get(position);
        }
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
            convertView = inflater.inflate(R.layout.team_list_item, null);
            TextView teamID = (TextView)convertView.findViewById(R.id.team_id);
            TextView teamTime = (TextView)convertView.findViewById(R.id.team_time);
            TextView teamDesc = (TextView)convertView.findViewById(R.id.team_description);
            TeamItem item = mTeamItems.get(position);
            teamID.setText(item.TeamIndex);
            teamTime.setText(item.StartDate);
            teamDesc.setText(item.TripDesc);
        }
        return convertView;
    }


}
