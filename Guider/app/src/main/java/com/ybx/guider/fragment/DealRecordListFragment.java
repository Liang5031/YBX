package com.ybx.guider.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.adapters.DealRecordListAdapter;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.DealRecordItem;
import com.ybx.guider.responses.DealRecordResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * create an instance of this fragment.
 */
public class DealRecordListFragment extends ListFragment implements Response.Listener<DealRecordResponse>, Response.ErrorListener {
    private DealRecordListAdapter mAdapter;
    private static String ARG_TEAM_ITEM = "team_item";
    TeamItem mTeamItem;
    TextView mEmptyView;
    XMLRequest<DealRecordResponse> mRequest;
    ArrayList<DealRecordItem> mAllItems;
    ArrayList<DealRecordItem> mTodayItems;
    RadioButton mRBToday;

    public DealRecordListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DealRecordListFragment.
     */
    public static DealRecordListFragment newInstance(TeamItem item) {
        DealRecordListFragment fragment = new DealRecordListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamItem = (TeamItem) getArguments().getSerializable(ARG_TEAM_ITEM);
        }

        mAllItems = new ArrayList<DealRecordItem>();
        mTodayItems = new ArrayList<DealRecordItem>();
        mAdapter = new DealRecordListAdapter(this.getContext(), mAllItems);
        this.setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deal_record_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView) this.getView().findViewById(R.id.teamOrderNumber)).setText(ResponseUtils.getTeamOrderNumber(mTeamItem.TeamOrderNumber));
        ((TextView) this.getView().findViewById(R.id.duration)).setText(ResponseUtils.getDuration(mTeamItem.StartDate, mTeamItem.EndDate));
        setCount(mAllItems.size());
        mEmptyView = (TextView) this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(mEmptyView);

        this.getView().findViewById(R.id.rbAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.update(mAllItems);
            }
        });

        mRBToday = (RadioButton) this.getView().findViewById(R.id.rbToday);
        mRBToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTodayItems.size() == 0) {
                    mEmptyView.setText("当日无成交记录！");
                }
//                mAdapter = new TeamScheduleListAdapter(TeamScheduleFragment.this.getContext(), mTodayItems);
//                TeamScheduleFragment.this.setListAdapter(mAdapter);
                mAdapter.update(mTodayItems);

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAllItems.clear();
            mTodayItems.clear();
            requestRealNameInfo(ParamUtils.VALUE_FIRST_PAGE_INDEX);
        }
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

    void requestRealNameInfo(Integer page) {
        Param param = new Param(ParamUtils.PAGE_TEAM_DEAL_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setTeamIndex(mTeamItem.TeamIndex);
        param.setPageIndex(page.toString());

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<DealRecordResponse>(url, this, this, new DealRecordResponse());
        mRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this.getContext()).add(mRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        setCount(0);
        if (!Utils.isNetworkAvailable(this.getContext())) {
            mEmptyView.setText("请检查网络连接是否可用！");
        } else {
            mEmptyView.setText("请求失败！");
        }

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    boolean isToday(String date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String today = s.format(new Date());
        return today.equalsIgnoreCase(date);
    }

    @Override
    public void onResponse(DealRecordResponse response) {
        if (response != null && response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            for (DealRecordItem item : response.mItems) {
                mAllItems.add(item);
                if (isToday(item.recDate)) {
                    mTodayItems.add(item);
                }
            }

            if (1 == response.mIsLastPage) {
                if (mRBToday.isChecked()) {
                    mAdapter.update(mTodayItems);
                } else {
                    mAdapter.update(mAllItems);
                }
                mEmptyView.setText("");
                setCount(mAllItems.size());
            } else {
                requestRealNameInfo(response.mPageIndex + 1);
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
        String countStr = String.format("本团队共有%d条成交记录", count);
        ((TextView) this.getView().findViewById(R.id.dealRecordNumber)).setText(countStr);
    }
}
