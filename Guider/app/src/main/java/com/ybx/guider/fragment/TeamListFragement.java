package com.ybx.guider.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ybx.guider.R;
import com.ybx.guider.activity.MainActivity;
import com.ybx.guider.activity.TeamActivity;
import com.ybx.guider.responses.BaseResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenl on 2016/2/11.
 */
public class TeamListFragement extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_team_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] from = new String[] {"team_id", "team_description"};
        final int[]  to = new int[] {R.id.team_id, R.id.team_description};

        SimpleAdapter adapter = new SimpleAdapter(
                this.getActivity(), getSimpleData(),
                R.layout.team_list_item, from, to);
        this.setListAdapter(adapter);

    }

    public void onListItemClick(ListView parent, View v,
                                int position, long id) {
        Intent intent = new Intent(this.getContext(), TeamActivity.class);
        intent.putExtra(TeamActivity.EXTRA_TEAM_ID, 1);
        startActivity(intent);
    }

    private List<Map<String, Object>> getSimpleData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("team_id", "001");
        map.put("team_description", "天门山1日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "002");
        map.put("team_description", "天门山2日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "003");
        map.put("team_description", "天门山3日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "004");
        map.put("team_description", "天门山4日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("team_id", "005");
        map.put("team_description", "天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游天门山5日游");
        list.add(map);

        return list;
    }
}
