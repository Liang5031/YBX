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
import com.ybx.guider.dialog.AcceptTeamDialog;
import com.ybx.guider.dialog.FinishTeamDialog;

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
public class TeamInfoFragment extends ListFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TeamInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamInfoFragment newInstance() {
        TeamInfoFragment fragment = new TeamInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.team_info_fragment_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        final String[] from = new String[] {"lable", "value"};
        final int[]  to = new int[] {R.id.lable, R.id.value};

        SimpleAdapter adapter = new SimpleAdapter(
                this.getActivity(), getSimpleData(),
                R.layout.team_info_list_item, from, to);
        this.setListAdapter(adapter);
        setHasOptionsMenu(true);
        this.getActivity().setTitle(R.string.title_info);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<Map<String, Object>> getSimpleData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lable", "团队编号");
        map.put("value", "249572345720");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "行程单编号（自编号）");
        map.put("value", "1234234134134134");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "总人数");
        map.put("value", "7");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "成年人数");
        map.put("value", "5");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "未成年人数");
        map.put("value", "2");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "成员描述");
        map.put("value", "5大2小，含两个老年人");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "行程日期");
        map.put("value", "2016-01-01至2016-01-07");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "行程简述");
        map.put("value", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "说明");
        map.put("value", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "旅行社");
        map.put("value", "天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "部门");
        map.put("value", "天门山5日游天门山");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "计调");
        map.put("value", "张三");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "客源地");
        map.put("value", "长沙");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "团队来源");
        map.put("value", "长沙");
        list.add(map);


        map = new HashMap<String, Object>();
        map.put("lable", "团队来源说明");
        map.put("value", "未知");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("lable", "状态");
        map.put("value", "未知");
        list.add(map);
        return list;
    }
}
