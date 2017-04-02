package com.starlabs.h2o.controller.purity_report;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starlabs.h2o.R;
import com.starlabs.h2o.adapter.ViewPurityReportsAdapter;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityReport;

import java.util.List;
import java.util.function.Consumer;

/**
 * A fragment for viewing the purity reports as a list
 *
 * @author chase
 */
public class ViewPurityReportsFragment extends Fragment {

    private RecyclerView mRecycler;
    private ViewPurityReportsAdapter adapter;

    public ViewPurityReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_purity_reports, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.purity_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Obtain list of Water Purity Reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<PurityReport>> onWaterReportsReceived = waterPurityReports -> {
            adapter = new ViewPurityReportsAdapter(waterPurityReports);
            mRecycler.setAdapter(adapter);
        };
        contentProvider.getAllPurityReports(onWaterReportsReceived);

        return view;
    }
}
