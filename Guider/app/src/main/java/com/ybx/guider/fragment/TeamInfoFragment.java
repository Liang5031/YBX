package com.ybx.guider.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.ybx.guider.R;
import com.ybx.guider.activity.TeamActivity;
import com.ybx.guider.dialog.AcceptTeamDialog;
import com.ybx.guider.dialog.FinishTeamDialog;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.TeamItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamInfoFragment extends ListFragment {
    private static String FIELD_LABEL = "label";
    private static String FIELD_VALUE = "value";

    private OnFragmentInteractionListener mListener;

    public TeamInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamInfoFragment newInstance() {
        TeamInfoFragment fragment = new TeamInfoFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.team_info_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.team_info_accept:
                AcceptTeamDialog newFragment = new AcceptTeamDialog();
                newFragment.show(getActivity().getSupportFragmentManager(), "accept");
                return true;
            case R.id.team_info_finish:
                FinishTeamDialog fragment = new FinishTeamDialog();
                fragment.show(getActivity().getSupportFragmentManager(), "finish");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] from = new String[]{FIELD_LABEL, FIELD_VALUE};
        final int[] to = new int[]{R.id.label, R.id.value};

        TeamItem item = ((TeamActivity) this.getActivity()).mTeamItem;

        SimpleAdapter adapter = new SimpleAdapter(
                this.getActivity(), getData(item),
                R.layout.team_info_list_item, from, to);
        this.setListAdapter(adapter);
        setHasOptionsMenu(true);
        this.getActivity().setTitle(R.string.title_info);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    String getCountStr(String adultCount, String childCount) {
        Integer adult = Integer.valueOf(adultCount);
        Integer child = Integer.valueOf(childCount);
        Integer total = adult + child;
        String countStr = "";

        if (total > 0) {
            countStr += "共" + total + "人，";
        }

        if (adult > 0) {
            countStr += adult + "成人";
        }

        if (child > 0) {
            countStr += child + "小孩";
        }

        return countStr;
    }

    private List<Map<String, Object>> getData(TeamItem item) {

        if (item == null)
            return null;

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "行程概述:");
        map.put("value", item.TripDesc);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "起止日期:");
        map.put("value", ResponseUtils.formatDate(item.StartDate) + " - " + ResponseUtils.formatDate(item.EndDate));
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "行程单号:");
        map.put("value", item.TeamOrderNumber);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "游客人数:");
        map.put("value", getCountStr(item.PepleCount1, item.PepleCount2));
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "团员说明:");
        map.put("value", item.MemberDesc);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "团款总额:");
        map.put("value", item.TotalAmount);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "收款提醒:");
        map.put("value", item.TotalAmountDesc);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "来源渠道:");
        map.put("value", item.TeamFrom);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "客源地:");
        map.put("value", item.Touristsource);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "旅行社:");
        map.put("value", item.AgencyName);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "部门:");
        map.put("value", item.DepartmentName);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "计调:");
        map.put("value", item.CName);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "备注:");
        map.put("value", item.memo);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "状态:");
        map.put("value", item.StatusName);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(FIELD_LABEL, "系统代码:");
        map.put("value", item.TeamIndex);
        list.add(map);

        return list;
    }
}
