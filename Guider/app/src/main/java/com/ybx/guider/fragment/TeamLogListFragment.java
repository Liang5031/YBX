package com.ybx.guider.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.RealNameListAdapter;
import com.ybx.guider.adapters.TeamLogListAdapter;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.RealNameItem;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamLogItem;
import com.ybx.guider.responses.TeamLogResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

/**
 * Use the {@link TeamLogListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamLogListFragment extends ListFragment implements Response.Listener<TeamLogResponse>, Response.ErrorListener {
    private TeamLogListAdapter mAdapter;
    private static String ARG_TEAM_ITEM = "team_item";
    TeamItem mTeamItem;
    TextView mEmptyView;
    XMLRequest<TeamLogResponse> mRequest;
    ArrayList<TeamLogItem> mAllItems;


    public TeamLogListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamLogListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamLogListFragment newInstance(TeamItem item) {
        TeamLogListFragment fragment = new TeamLogListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mTeamItem != null) {
            ((TextView) this.getView().findViewById(R.id.duration)).setText(ResponseUtils.getDuration(mTeamItem.StartDate, mTeamItem.EndDate));
            ((TextView) this.getView().findViewById(R.id.teamOrderNumber)).setText(ResponseUtils.getTeamOrderNumber(mTeamItem.TeamOrderNumber));
        }

        mEmptyView = (TextView) this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(mEmptyView);
        setCount(0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAllItems.clear();
            requestTeamLog(ParamUtils.VALUE_FIRST_PAGE_INDEX, false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mTeamItem = (TeamItem) getArguments().getSerializable(ARG_TEAM_ITEM);
        }
        mAllItems = new ArrayList<TeamLogItem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_log_list, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mRequest!=null) {
            mRequest.cancel();
        }
    }

    void requestTeamLog(Integer page, boolean isRefresh) {
        Param param = new Param(ParamUtils.PAGE_TEAM_LOG_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setTeamIndex(mTeamItem.TeamIndex);
        param.setPageIndex(page.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<TeamLogResponse>(url, this, this, new TeamLogResponse());
        mRequest.setShouldCache(true);
        if (isRefresh) {
            /* remove cache first */
            VolleyRequestQueue.getInstance(this.getContext()).remove(url);
        }
        VolleyRequestQueue.getInstance(this.getContext()).add(mRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mEmptyView.setText("请求失败！");
        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(TeamLogResponse response) {
        if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (TeamLogItem item : response.mItems) {
                mAllItems.add(item);
            }

            if (1 == response.mIsLastPage) {
                mAdapter = new TeamLogListAdapter(this.getContext(), mAllItems);
                this.setListAdapter(mAdapter);
                setCount(mAllItems.size());
            } else {
                requestTeamLog(response.mPageIndex + 1, false);
            }
        } else {
            mEmptyView.setText(response.mReturnMSG);
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }

    void setCount(int count) {
        String FORMAT = "本团队共有%d个日志信息";
        ((TextView) this.getView().findViewById(R.id.teamLogNumber)).setText(String.format(FORMAT, count));
    }
}
