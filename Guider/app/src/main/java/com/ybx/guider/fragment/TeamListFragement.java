package com.ybx.guider.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.activity.TeamActivity;
import com.ybx.guider.adapters.TeamListAdapter;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamQueryResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/11.
 */
public class TeamListFragement extends ListFragment implements Response.Listener<TeamQueryResponse>, Response.ErrorListener {
    private TeamListAdapter mAdapter;
    private static String ARG_TEAM_STATUS = "team_status";
    public static int TEAM_STATUS_ONGOING = 4;  /* 4 -- 导游已接团 */
    public static int TEAM_STATUS_WAITING = 3;  /* 3 -- 已委派待接团 */
    public static int TEAM_STATUS_FINISHED = 5; /* 5 - 带团完成待结算 */
    public ArrayList<TeamItem> mAllTeamItems;
    TextView mEmptyView;

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
        mAllTeamItems = new ArrayList<TeamItem>();
    }

    public void onListItemClick(ListView parent, View v,
                                int position, long id) {
        if (mAdapter != null) {
            TeamItem item = (TeamItem) mAdapter.getItem(position);
            Intent intent = new Intent(this.getContext(), TeamActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(TeamActivity.EXTRA_TEAM_ITEM, item);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEmptyView = (TextView)this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(mEmptyView);
        requestTeamList(mTeamStatus, 0);
        mAllTeamItems.clear();
    }

//    public void requestData(){
//        requestTeamList(mTeamStatus, 0);
//    }

    public void requestTeamList(Integer status,Integer page) {
        Param param = new Param(ParamUtils.PAGE_TEAM_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setTeamStatus(status.toString());
        if(page != 0 ) {
            param.setPageIndex(page.toString());
        }

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        XMLRequest<TeamQueryResponse> request = new XMLRequest<TeamQueryResponse>(url, this, this, new TeamQueryResponse());
        request.setShouldCache(false);
        VolleyRequestQueue.getInstance(this.getContext()).add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Toast.makeText(this.getContext(), "获取团队列表失败！", Toast.LENGTH_LONG).show();
        mEmptyView.setText("网络连接失败，请检查网络连接是否可用！");
        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(TeamQueryResponse response) {
        if(response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (TeamItem item : response.mTeamItems) {
                mAllTeamItems.add(item);
            }

            if(1== response.mIsLastPage) {
                mAdapter = new TeamListAdapter(this.getContext(), mAllTeamItems);
                this.setListAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
            } else {
                requestTeamList(mTeamStatus, response.mPageIndex+1);
            }
        } else {
            mEmptyView.setText(response.mReturnMSG);
//            Toast.makeText(this.getContext(), "获取团队列表失败！", Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }
}
