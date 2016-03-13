package com.ybx.guider.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.ybx.guider.R;
import com.ybx.guider.adapters.DeptListAdapter;

public class DeptManageActivity extends AppCompatActivity {

    ListView mListView;
    DeptListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_manage);
        this.setTitle("旅行社/部门列表");

        mListView = (ListView)findViewById(R.id.dept_list);
        mAdapter = new DeptListAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setEmptyView(findViewById(R.id.empty));
    }

    void requestDeptList(){

    }



}
