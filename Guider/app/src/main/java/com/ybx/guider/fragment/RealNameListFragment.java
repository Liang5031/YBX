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
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.RealNameInfoResponse;
import com.ybx.guider.responses.RealNameItem;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

/**
 * Use the {@link RealNameListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RealNameListFragment extends ListFragment implements Response.Listener<RealNameInfoResponse>, Response.ErrorListener {
    private static String ARG_TEAM_ITEM = "team_item";
    TeamItem mTeamItem;
    TextView mEmptyView;
    XMLRequest<RealNameInfoResponse> mRequest;
    RealNameListAdapter mAdapter;
    ArrayList<RealNameItem> mAllItems;

    public RealNameListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RealNameListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RealNameListFragment newInstance(TeamItem item) {
        RealNameListFragment fragment = new RealNameListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        LayoutInflater inflater = LayoutInflater.from(this.getContext());
//        this.getListView().addHeaderView(inflater.inflate(R.layout.real_name_list_header, null));
//        ((TextView) this.getView().findViewById(R.id.teamIndex)).setText(mTeamIndex);
        if (mTeamItem != null) {
            ((TextView) this.getView().findViewById(R.id.duration)).setText(ResponseUtils.getDuration(mTeamItem.StartDate, mTeamItem.EndDate));
            ((TextView) this.getView().findViewById(R.id.teamOrderNumber)).setText(ResponseUtils.getTeamOrderNumber(mTeamItem.TeamOrderNumber));
        }


        mEmptyView = (TextView) this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(mEmptyView);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAllItems.clear();
            requestRealNameInfo(ParamUtils.VALUE_FIRST_PAGE_INDEX, false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamItem = (TeamItem) getArguments().getSerializable(ARG_TEAM_ITEM);
        }
        setHasOptionsMenu(true);
        mAllItems = new ArrayList<RealNameItem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_real_name_list, container, false);
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
    public void onDestroy() {
        super.onDestroy();
        if (mRequest != null) {
            mRequest.cancel();
        }
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

    void requestRealNameInfo(Integer page, boolean isRefresh) {
        Param param = new Param(ParamUtils.PAGE_TEAM_REAL_NAME_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setTeamIndex(mTeamItem.TeamIndex);
        param.setPageIndex(page.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<RealNameInfoResponse>(url, this, this, new RealNameInfoResponse());
//        mRequest.setShouldCache(false);
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
    public void onResponse(RealNameInfoResponse response) {
        if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (RealNameItem item : response.mItems) {
                mAllItems.add(item);
            }

            if (1 == response.mIsLastPage) {
                mAdapter = new RealNameListAdapter(this.getContext(), mAllItems);
                this.setListAdapter(mAdapter);
                setCount(mAllItems.size());
            } else {
                requestRealNameInfo(response.mPageIndex + 1, false);
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
        String FORMAT = "本团队共有%1$d个实名信息";
        ((TextView) this.getView().findViewById(R.id.realNameCount)).setText(String.format(FORMAT, count));
    }
}
