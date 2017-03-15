package com.starlabs.h2o.controller.water_report;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.adapter.ViewWaterReportsAdapter;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class to view water reports in a recycler view
 *
 * @author Rishi
 */
public class ViewWaterReportsActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private ViewWaterReportsAdapter adapter;
    private List<WaterReport> waterReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Obtain list of Water Reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<WaterReport>> onWaterReportsReceived = new Consumer<List<WaterReport>>() {
            @Override
            public void accept(List<WaterReport> waterReports) {
                adapter = new ViewWaterReportsAdapter(waterReports);
                mRecycler.setAdapter(adapter);
            }
        };
        contentProvider.getAllWaterReports(onWaterReportsReceived);
    }
}
