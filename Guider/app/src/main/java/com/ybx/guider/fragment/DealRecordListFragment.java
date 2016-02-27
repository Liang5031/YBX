package com.ybx.guider.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ybx.guider.R;
import com.ybx.guider.adapters.DealRecordListAdapter;

/**
 * create an instance of this fragment.
 */
public class DealRecordListFragment extends ListFragment {
    private DealRecordListAdapter mAdapter;

    public DealRecordListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DealRecordListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DealRecordListFragment newInstance() {
        DealRecordListFragment fragment = new DealRecordListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        View emptyView = this.getView().findViewById(R.id.empty);
        this.getListView().setEmptyView(emptyView);
        mAdapter = new DealRecordListAdapter(this.getContext());
        this.setListAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
