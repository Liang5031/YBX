package com.ybx.guider.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ybx.guider.R;
import com.ybx.guider.activity.TeamActivity;
import com.ybx.guider.adapters.TeamListAdapter;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

/**
 * Created by chenl on 2016/2/11.
 */
public class TeamListFragement extends ListFragment {
    private TeamListAdapter mAdapter;
    private static String ARG_TEAM_STATUS = "team_status";
    public static int TEAM_STATUS_ONGOING = 1;  /* 4 -- 导游已接团 */
    public static int TEAM_STATUS_WAITING = 2;  /* 3 -- 已委派待接团 */
    public static int TEAM_STATUS_FINISHED = 3; /* 5 - 带团完成待结算 */

    private int mTeamStatus;

    public static TeamListFragement newInstance(int teamStatus) {
        TeamListFragement fragment = new TeamListFragement();
        Bundle args = new Bundle();
        args.putInt(ARG_TEAM_STATUS, teamStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_team_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamStatus = getArguments().getInt(ARG_TEAM_STATUS);
        }
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
//        mAdapter = new TeamListAdapter(this.getContext());
//        this.setListAdapter(mAdapter);
    }

    public void requestTeamList(int status){
        Param param = new Param(ParamUtils.PAGE_GUIDER_REGISTER);
        param.removeParam(ParamUtils.KEY_SIGN_TYPE);

        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));

//        if( status )
//        param.setTeamStatus(status);

//        String orderParams = param.getParamStringInOrder();
//        String sign = EncryptUtils.generateSign(orderParams, password);
//        param.setSign(sign);

//        XMLRequest request = new XMLRequest(URLUtils.generateURL(ParamUtils.PAGE_GUIDER_REGISTER, param), this, this);
//        VolleyRequestQueue.getInstance(this.getContext()).add(request);

    }
}
