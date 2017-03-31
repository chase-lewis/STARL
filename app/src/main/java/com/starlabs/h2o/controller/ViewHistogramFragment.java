package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.starlabs.h2o.R;

import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Kavin on 3/27/2017.
 */

public class ViewHistogramFragment extends Fragment {
    private Spinner yAxisSpinner;
    private TextView histogramTitle;
    private String spinnerVal;

    private FragmentManager fragmentManager = getFragmentManager();

    public ViewHistogramFragment(){
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_histogram, container, false);

        yAxisSpinner = (Spinner) view.findViewById(R.id.histogramSlider);

        List<String> yaxisChoices = new ArrayList<>();
        yaxisChoices.add("Virus");
        yaxisChoices.add("Contaminant");

        ArrayAdapter<String> yaxisAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, yaxisChoices);
        yaxisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yAxisSpinner.setAdapter(yaxisAdapter);

        Bundle b = getArguments();
        String initYAxis = b.getString("spinner_val");
        int reportYear = b.getInt("select_year");
        final List<Integer> purityReportIds = b.getIntegerArrayList("purity_report_ids");

        // Obtain list of Purity Reports from the content provider using the ids
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<PurityReport>> waterReportsReceived = allPurityReports -> {
            List<PurityReport> filteredPurityReports = new ArrayList<>();

            // Filter out the purity reports and only use the ones with the matching ids
            for (PurityReport pReport : allPurityReports){
                boolean reportFound = false;
                int i = 0;
                while (!reportFound && i < purityReportIds.size()){
                    if (pReport.getReportNumber() == purityReportIds.get(i)){
                        reportFound = true;
                        filteredPurityReports.add(pReport);
                    }
                    i++;
                }
            }

            LineGraphSeries<DataPoint> viruses = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> contam = new LineGraphSeries<>();

            filteredPurityReports.stream().filter(current -> (reportYear - 1900) == current.getCreationDate().getYear()).forEach(current -> {
                int currRepNum = current.getReportNumber();
                int currVirNum = current.getVirusPPM();
                int currContNum = current.getContPPM();

                viruses.appendData(new DataPoint(currRepNum, currVirNum), true, 500);
                contam.appendData(new DataPoint(currRepNum, currContNum), true, 500);
            });

            yAxisSpinner.setSelection(yaxisChoices.indexOf(initYAxis));

            // Get whether user wants to see contamination or virus level
            String yaxis = (String)yAxisSpinner.getSelectedItem();

            // Set Title based on y axis
            String titleToShow = reportYear + " " + yaxis + " PPM Histogram";
            histogramTitle = (TextView) view.findViewById(R.id.histogramReportTitle);
            histogramTitle.setText(titleToShow);


            GraphView histogram = (GraphView) view.findViewById(R.id.histogramGraph);
            histogram.getGridLabelRenderer().setHorizontalAxisTitle("Purity Report ID");
            histogram.removeAllSeries();
            if (yaxis.equals("Virus")) {
                histogram.addSeries(viruses);
            } else {
                histogram.addSeries(contam);
            }

            Button updateHistogramButton = (Button) view.findViewById(R.id.updateHistogramButton);
            updateHistogramButton.setOnClickListener(view1 -> {
                String newYaxis = (String)yAxisSpinner.getSelectedItem();
                histogram.removeAllSeries();
                if (newYaxis.equals("Virus")) {
                    histogram.addSeries(viruses);
                } else {
                    histogram.addSeries(contam);
                }
                String titleToShow1 = reportYear + " " + newYaxis + " PPM Histogram";
                histogramTitle.setText(titleToShow1);
            });

            // Cancel button setup
            Button reportCancelButton = (Button) view.findViewById(R.id.histogramCancel);
            reportCancelButton.setOnClickListener(ViewHistogramFragment.this::onCancelPressed);
        };
        contentProvider.getAllPurityReports(waterReportsReceived);
        return view;
    }

    /**
     * Method to exit the activity back to caller.
     *
     * @param view the parameter View
     */
    protected void onCancelPressed(View view) {
        getActivity().onBackPressed();
    }

}
