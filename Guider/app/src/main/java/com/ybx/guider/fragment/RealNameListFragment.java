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
import com.ybx.guider.adapters.RealNameListAdapter;

/**
 * Use the {@link RealNameListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RealNameListFragment extends ListFragment {
    private RealNameListAdapter mAdapter;

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
    public static RealNameListFragment newInstance() {
        RealNameListFragment fragment = new RealNameListFragment();
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
        mAdapter = new RealNameListAdapter(this.getContext());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.team_schedule_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.schedule_fragment_refresh:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
