package com.starlabs.h2o.controller.purity_report;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.adapter.ViewPurityReportsAdapter;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityReport;

import java.util.List;
import java.util.function.Consumer;

public class ViewPurityReportsActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private ViewPurityReportsAdapter adapter;

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
                adapter = new ViewPurityReportsAdapter(waterPurityReports);
                mRecycler.setAdapter(adapter);
            }
        };
        contentProvider.getAllPurityReports(onWaterReportsReceived);
    }
}
