package com.ybx.guider.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.activity.TeamActivity;
import com.ybx.guider.adapters.TeamListAdapter;
import com.ybx.guider.dialog.AcceptTeamDialog;
import com.ybx.guider.dialog.FinishTeamDialog;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamListResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by chenl on 2016/2/11.
 */
public class TeamListFragement extends ListFragment implements Response.Listener<TeamListResponse>, Response.ErrorListener {

    private static String ARG_TEAM_STATUS = "team_status";
    public static int TEAM_STATUS_ONGOING = 4;  /* 4 -- 导游已接团 */
    public static int TEAM_STATUS_WAITING = 3;  /* 3 -- 已委派待接团 */
    public static int TEAM_STATUS_FINISHED = 5; /* 5 - 带团完成待结算 */
    public ArrayList<TeamItem> mAllTeamItems;

    TextView mEmptyView;
    XMLRequest<TeamListResponse> mRequest;
    TeamListAdapter mAdapter;
    int mTeamStatus;
    private View mLoadMoreView;
    int mPageIndex;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAllTeamItems.clear();
            requestTeamList(mTeamStatus, ParamUtils.VALUE_FIRST_PAGE_INDEX, false);
        }
    }

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.team_info_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.team_info_accept:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamStatus = getArguments().getInt(ARG_TEAM_STATUS);
        }
        setHasOptionsMenu(true);
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

        mEmptyView = (TextView) this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(mEmptyView);


        mLoadMoreView = getActivity().getLayoutInflater().inflate(R.layout.team_list_footer, null);
        Button loadMoreButton = (Button)mLoadMoreView.findViewById(R.id.btnLoadMore);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestTeamList(mTeamStatus, mPageIndex + 1, false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mRequest!=null) {
            mRequest.cancel();
        }
    }

    public void requestTeamList(Integer status, Integer page, boolean isRefresh) {
        if (this.getContext() == null) {
            return;
        }

        Param param = new Param(ParamUtils.PAGE_TEAM_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setTeamStatus(status.toString());
        param.setPageIndex(page.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<TeamListResponse>(url, this, this, new TeamListResponse());
        mRequest.setShouldCache(true);
        if (isRefresh) {
            /* remove cache first */
            VolleyRequestQueue.getInstance(this.getContext()).remove(url);
        }

        VolleyRequestQueue.getInstance(this.getContext()).add(mRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Toast.makeText(this.getContext(), "获取团队列表失败！", Toast.LENGTH_LONG).show();
        mEmptyView.setText("请求失败！");
        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(TeamListResponse response) {
        if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (TeamItem item : response.mItems) {
                mAllTeamItems.add(item);
            }
            mPageIndex = response.mPageIndex;

            if (1 == response.mIsLastPage) {
                mAdapter = new TeamListAdapter(this.getContext(), mAllTeamItems);
                this.getListView().removeFooterView(mLoadMoreView);
                this.setListAdapter(mAdapter);
            } else {
                mAdapter = new TeamListAdapter(this.getContext(), mAllTeamItems);
                this.getListView().removeFooterView(mLoadMoreView);
                this.getListView().addFooterView(mLoadMoreView);
                this.setListAdapter(mAdapter);
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
