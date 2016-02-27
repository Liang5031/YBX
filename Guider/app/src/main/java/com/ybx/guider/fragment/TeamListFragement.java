package com.ybx.guider.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.activity.MainActivity;
import com.ybx.guider.activity.TeamActivity;
import com.ybx.guider.adapters.DealRecordListAdapter;
import com.ybx.guider.adapters.TeamListAdapter;
import com.ybx.guider.responses.BaseResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenl on 2016/2/11.
 */
public class TeamListFragement extends ListFragment {
    private TeamListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_team_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onListItemClick(ListView parent, View v,
                                int position, long id) {
        Intent intent = new Intent(this.getContext(), TeamActivity.class);
        intent.putExtra(TeamActivity.EXTRA_TEAM_ID, 1);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View emptyView = this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(emptyView);
        mAdapter = new TeamListAdapter(this.getContext());
        this.setListAdapter(mAdapter);
    }
}
