package com.starlabs.h2o.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.adapter.ViewWaterPurityReportsAdapter;
import com.starlabs.h2o.adapter.ViewWaterReportsAdapter;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ViewPurityWaterReportsActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private ViewWaterPurityReportsAdapter adapter;
    private List<PurityReport> waterPurityReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purity_water_reports);

        mRecycler = (RecyclerView) findViewById(R.id.purity_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Obtain list of Water Purity Reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<PurityReport>> onWaterReportsReceived = new Consumer<List<PurityReport>>() {
            @Override
            public void accept(List<PurityReport> waterPurityReports) {
                adapter = new ViewWaterPurityReportsAdapter(waterPurityReports);
                mRecycler.setAdapter(adapter);
            }
        };
        contentProvider.getAllPurityReports(onWaterReportsReceived);
    }
}
