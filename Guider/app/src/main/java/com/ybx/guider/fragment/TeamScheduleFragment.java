package com.ybx.guider.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.TeamScheduleListAdapter;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.responses.TeamScheduleItem;
import com.ybx.guider.responses.TeamScheduleListResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.util.ArrayList;

/**
 * create an instance of this fragment.
 */
public class TeamScheduleFragment extends ListFragment implements Response.Listener<TeamScheduleListResponse>, Response.ErrorListener {
    static final int MENU_ITEM_SYNC = 1;
    static final int MENU_ITEM_CANCEL_SCHEDULE = 2;
    static final int MENU_ITEM_FINISH_SCHEDULE = 3;
    static final int MENU_ITEM_CHANGE_RESERVATION = 4;
    static final int MENU_ITEM_MAKE_RESERVATION = 5;
    static final int MENU_ITEM_CANCEL_RESERVATION = 6;
    static final String ARG_TEAM_ITEM = "team_item";


    TeamScheduleListAdapter mAdapter;
    ArrayList<TeamScheduleItem> mAllTeamSchduleItems;
    XMLRequest<TeamScheduleListResponse> mRequest;

    TeamItem mTeamItem;
    TextView mEmptyView;

    public TeamScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamScheduleFragment newInstance(TeamItem item) {
        TeamScheduleFragment fragment = new TeamScheduleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

//        menu.setHeaderTitle("文件操作");
        // add context menu item
//        menu.add(0, MENU_ITEM_SYNC, Menu.NONE, R.string.menu_sync);
        menu.add(0, MENU_ITEM_CANCEL_SCHEDULE, Menu.NONE, R.string.menu_cancel_schedule);
        menu.add(0, MENU_ITEM_FINISH_SCHEDULE, Menu.NONE, R.string.menu_finish_schedule);
        menu.add(0, MENU_ITEM_CHANGE_RESERVATION, Menu.NONE, R.string.menu_cancel_reservation);
        menu.add(0, MENU_ITEM_MAKE_RESERVATION, Menu.NONE, R.string.menu_make_reservation);
        menu.add(0, MENU_ITEM_CANCEL_RESERVATION, Menu.NONE, R.string.menu_cancel_reservation);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                // do something
                break;
            case 2:
                // do something
                break;
            case 3:
                // do something
                break;
            case 4:
                // do something
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamItem = (TeamItem) getArguments().getSerializable(ARG_TEAM_ITEM);
        }
        mAllTeamSchduleItems = new ArrayList<TeamScheduleItem>();
        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);

        mAdapter.changeVisibility(v, position);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_schedule, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAllTeamSchduleItems.clear();
        mEmptyView = (TextView) this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(mEmptyView);
        this.registerForContextMenu(getListView());

        if (mTeamItem != null) {
            ((TextView) this.getView().findViewById(R.id.duration)).setText(ResponseUtils.getDuration(mTeamItem.StartDate, mTeamItem.EndDate));
            ((TextView) this.getView().findViewById(R.id.teamOrderNumber)).setText(ResponseUtils.getTeamOrderNumber(mTeamItem.TeamOrderNumber));
            ((TextView) this.getView().findViewById(R.id.teamDesc)).setText(mTeamItem.TripDesc);
        }
        requestTeamScheduleList(mTeamItem.TeamIndex, ParamUtils.VALUE_FIRST_PAGE_INDEX, false);
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
        mRequest.cancel();
    }

    void setScheduleCount(int count) {
//        String scheduleNumber = getResources().getString(R.string.schedule_number);
//        scheduleNumber = String.format(scheduleNumber, count);
//        ((TextView) this.getView().findViewById(R.id.scheduleNumber)).setText(scheduleNumber);
    }

    void requestTeamScheduleList(String teamIndex, Integer page, boolean isRefresh) {
        Param param = new Param(ParamUtils.PAGE_TEAM_SCHEDULE_QUERY);

        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setTeamIndex(teamIndex);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<TeamScheduleListResponse>(url, this, this, new TeamScheduleListResponse());
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
    public void onResponse(TeamScheduleListResponse response) {
        if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (TeamScheduleItem item : response.mItems) {
                mAllTeamSchduleItems.add(item);
            }

            if (1 == response.mIsLastPage) {
                mAdapter = new TeamScheduleListAdapter(this.getContext(), mAllTeamSchduleItems);
                this.setListAdapter(mAdapter);
                setScheduleCount(mAllTeamSchduleItems.size());
            } else {
                requestTeamScheduleList(mTeamItem.TeamIndex, response.mPageIndex + 1, false);
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
