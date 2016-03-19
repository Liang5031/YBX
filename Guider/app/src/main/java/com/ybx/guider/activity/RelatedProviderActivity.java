package com.ybx.guider.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ybx.guider.R;
import com.ybx.guider.adapters.RelatedProviderAdapter;

public class RelatedProviderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mListView;
    RelatedProviderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_provider);

        this.setTitle("供应商列表");

        mListView = (ListView) findViewById(R.id.dept_list);
        mAdapter = new RelatedProviderAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
