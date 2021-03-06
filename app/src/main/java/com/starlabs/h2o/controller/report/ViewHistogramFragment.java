package com.starlabs.h2o.controller.report;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.facade.ReportManager;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Displays the histogram
 *
 * @author tejun, rishi, kavin
 */
public class ViewHistogramFragment extends Fragment {
    private final int MAX_DATA = 500;
    private Spinner yAxisSpinnerView;
    private TextView histogramTitleView;
    private GraphView histogramView;
    private LineGraphSeries<DataPoint> virusData;
    private LineGraphSeries<DataPoint> contaminationData;
    private int reportYear;
    private WaterReport waterReport;

    /**
     * Default constructor with no args
     */
    public ViewHistogramFragment() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_histogram, container, false);

        // Set up views
        yAxisSpinnerView = (Spinner) view.findViewById(R.id.histogramSlider);
        histogramTitleView = (TextView) view.findViewById(R.id.histogramReportTitle);
        histogramView = (GraphView) view.findViewById(R.id.histogramGraph);

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.histogram_done);
        reportCancelButton.setOnClickListener((view1) ->
                ViewHistogramFragment.this.onCancelPressed());

        // Initialize the data points
        virusData = new LineGraphSeries<>();
        contaminationData = new LineGraphSeries<>();

        // Set up the spinner for choosing the y axis
        List<String> yAxisChoices = new ArrayList<>();
        yAxisChoices.add("Virus");
        yAxisChoices.add("Contaminant");
        ArrayAdapter<String> yAxisAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, yAxisChoices);
        yAxisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yAxisSpinnerView.setAdapter(yAxisAdapter);

        // Update the graph on spinner change
        yAxisSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                updateGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                updateGraph();
            }

        });

        // Get the arguments from the bundle
        Bundle b = getArguments();
        String initYAxis = b.getString("spinner_val");
        reportYear = b.getInt("select_year");

        // Get the facade (Report Manager)
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        ReportManager reportManager = ReportManager.getInstance(contentProvider);

        // Obtain list of Purity Reports from the Report Manager
        Consumer<List<PurityReport>> onReportsReceived =
                getFilteredWaterReports(yAxisChoices, initYAxis);
        reportManager.getLinkedPurityReportsWithYear(waterReport, reportYear, onReportsReceived);

        return view;
    }

    /**
     * Obtains filtered water reports
     *
     * @param yAxisChoices array of possible y axis choices
     * @param initYAxis    initial y axis choice for histogram
     * @return a Consumer<List<PurityReport> object
     */
    private Consumer<List<PurityReport>> getFilteredWaterReports(
            List<String> yAxisChoices, String initYAxis) {
        return allPurityReports -> {

            // Fill in the data
            for (PurityReport current : allPurityReports) {
                int currRepNum = current.getReportNumber();
                int currVirNum = current.getVirusPPM();
                int currContNum = current.getContPPM();

                virusData.appendData(new DataPoint(currRepNum, currVirNum), true, MAX_DATA);
                contaminationData.appendData(new DataPoint(currRepNum, currContNum),
                        true, MAX_DATA);
            }

            // Set the choice of data to view
            yAxisSpinnerView.setSelection(yAxisChoices.indexOf(initYAxis));

            // Get whether user wants to see contaminationData or virus level
            String yAxis = (String) yAxisSpinnerView.getSelectedItem();

            // Set Title based on y axis
            String titleToShow = reportYear + " " + yAxis + " PPM Histogram";
            histogramTitleView.setText(titleToShow);

            // Update the graph
            this.updateGraph();
        };
    }

    /**
     * Method to exit the activity back to caller.
     */
    private void onCancelPressed() {
        Activity act = getActivity();
        act.onBackPressed();
    }

    /**
     * Updates the graph. Called when data to view has changed.
     */
    private void updateGraph() {
        // Clear the graph
        histogramView.removeAllSeries();

        // Add the correct data points
        String newYAxis = (String) yAxisSpinnerView.getSelectedItem();
        if ("Virus".equals(newYAxis)) {
            histogramView.addSeries(virusData);
        } else {
            histogramView.addSeries(contaminationData);
        }

        // Update the title
        String titleToShow1 = reportYear + " " + newYAxis + " PPM Histogram";
        histogramTitleView.setText(titleToShow1);
    }

    /**
     * Setter for the current Water Report the data should be pulled from
     *
     * @param waterReport the water report to use
     */
    public void setWaterReport(WaterReport waterReport) {
        this.waterReport = waterReport;
    }
}
