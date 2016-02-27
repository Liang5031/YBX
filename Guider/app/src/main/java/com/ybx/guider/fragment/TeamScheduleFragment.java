package com.ybx.guider.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ybx.guider.R;
import com.ybx.guider.adapters.TeamScheduleListAdapter;

/**
 * create an instance of this fragment.
 */
public class TeamScheduleFragment extends ListFragment {
    private static final int MENU_ITEM_SYNC = 1;
    private static final int MENU_ITEM_CANCEL_SCHEDULE = 2;
    private static final int MENU_ITEM_FINISH_SCHEDULE = 3;
    private static final int MENU_ITEM_CHANGE_RESERVATION = 4;
    private static final int MENU_ITEM_MAKE_RESERVATION = 5;
    private static final int MENU_ITEM_CANCEL_RESERVATION= 6;

    private TeamScheduleListAdapter mAdapter;

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
    public static TeamScheduleFragment newInstance() {
        TeamScheduleFragment fragment = new TeamScheduleFragment();
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
        setHasOptionsMenu(true);
        mAdapter = new TeamScheduleListAdapter(this.getContext());
        this.setListAdapter(mAdapter);
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
        String scheduleNumber = getResources().getString(R.string.schedule_number);
        scheduleNumber = String.format(scheduleNumber, 23);
        TextView tv = (TextView) this.getActivity().findViewById(R.id.scheduleNumber);
        tv.setText(scheduleNumber);
        this.registerForContextMenu(getListView());

//        this.getActivity().findViewById(R.id.sync).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(TeamScheduleFragment.this.getContext(), "sync", Toast.LENGTH_LONG).show();
//            }
//        });
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
