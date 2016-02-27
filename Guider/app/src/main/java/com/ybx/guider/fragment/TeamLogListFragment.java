package com.ybx.guider.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ybx.guider.R;
import com.ybx.guider.adapters.TeamLogListAdapter;

/**
 * Use the {@link TeamLogListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamLogListFragment extends ListFragment {
    private TeamLogListAdapter mAdapter;

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
    public static TeamLogListFragment newInstance() {
        TeamLogListFragment fragment = new TeamLogListFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        LayoutInflater inflater = LayoutInflater.from(this.getContext());
//        this.getListView().addHeaderView(inflater.inflate(R.layout.real_name_list_header, null));

        View emptyView = this.getView().findViewById(R.id.empty);
//        emptyView.setVisibility(View.VISIBLE);
        this.getListView().setEmptyView(emptyView);
        mAdapter = new TeamLogListAdapter(this.getContext());
        this.setListAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
}
