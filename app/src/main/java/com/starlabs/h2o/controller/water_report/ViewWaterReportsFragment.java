package com.starlabs.h2o.controller.water_report;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starlabs.h2o.R;
import com.starlabs.h2o.adapter.ViewWaterReportsAdapter;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.List;
import java.util.function.Consumer;

/**
 * A fragment for viewing the water reports as a list
 *
 * @author chase
 */
public class ViewWaterReportsFragment extends Fragment {

    private RecyclerView mRecycler;
    private ViewWaterReportsAdapter adapter;
    private Consumer<List<WaterReport>> onWaterReportsReceived = waterReports -> {
        adapter = new ViewWaterReportsAdapter(waterReports);
        mRecycler.setAdapter(adapter);
    };


    /**
     * Default no arg constructor
     */
    public ViewWaterReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_water_reports, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.water_report_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Obtain list of Water Reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.getAllWaterReports(onWaterReportsReceived);

        return view;
    }
}
