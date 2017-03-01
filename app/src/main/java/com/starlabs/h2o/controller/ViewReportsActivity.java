package com.starlabs.h2o.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.WaterReport;

import java.util.List;

public class ViewReportsActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private RVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<WaterReport> waterReports;
        //TODO: Obtain list of Water Reports and put it in waterReports

        //adapter = new RVAdapter(waterReports);
        mRecycler.setAdapter(adapter);

    }
}
