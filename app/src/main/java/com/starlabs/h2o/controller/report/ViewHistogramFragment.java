package com.starlabs.h2o.controller.report;

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

import com.jjoe64.graphview.*;
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
    private Spinner yAxisSpinnerView;
    private TextView histogramTitleView;
    private GraphView histogramView;
    private LineGraphSeries<DataPoint> virusData;
    private LineGraphSeries<DataPoint> contaminationData;
    private int reportYear;
    private WaterReport waterReport;

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

        // Set up views
        yAxisSpinnerView = (Spinner) view.findViewById(R.id.histogramSlider);
        histogramTitleView = (TextView) view.findViewById(R.id.histogramReportTitle);
        histogramView = (GraphView) view.findViewById(R.id.histogramGraph);

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.histogram_done);
        reportCancelButton.setOnClickListener(ViewHistogramFragment.this::onCancelPressed);

        // Initialize the data points
        virusData = new LineGraphSeries<>();
        contaminationData = new LineGraphSeries<>();

        // Set up the spinner for choosing the y axis
        List<String> yaxisChoices = new ArrayList<>();
        yaxisChoices.add("Virus");
        yaxisChoices.add("Contaminant");
        ArrayAdapter<String> yaxisAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, yaxisChoices);
        yaxisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yAxisSpinnerView.setAdapter(yaxisAdapter);

        // Update the graph on spinner change
        yAxisSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateGraph(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                updateGraph(view);
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
        Consumer<List<PurityReport>> onReportsReceived = allPurityReports -> {

            // Filter by year, fill in the data
            allPurityReports.stream().filter(current -> (reportYear - 1900)
                    == current.getCreationDate().getYear()).forEach(current -> {
                int currRepNum = current.getReportNumber();
                int currVirNum = current.getVirusPPM();
                int currContNum = current.getContPPM();

                virusData.appendData(new DataPoint(currRepNum, currVirNum), true, 500);
                contaminationData.appendData(new DataPoint(currRepNum, currContNum), true, 500);
            });

            // Set the choice of data to view
            yAxisSpinnerView.setSelection(yaxisChoices.indexOf(initYAxis));

            // Get whether user wants to see contaminationData or virus level
            String yaxis = (String) yAxisSpinnerView.getSelectedItem();

            // Set Title based on y axis
            String titleToShow = reportYear + " " + yaxis + " PPM Histogram";
            histogramTitleView.setText(titleToShow);

            // Update the graph
            this.updateGraph(view);
        };
        reportManager.getLinkedPurityReports(waterReport, onReportsReceived);

        return view;
    }

    /**
     * Method to exit the activity back to caller.
     *
     * @param view the parameter View
     */
    private void onCancelPressed(View view) {
        getActivity().onBackPressed();
    }

    /**
     * Updates the graph. Call when data to view has changed
     *
     * @param view the view
     */
    private void updateGraph(View view) {
        // Clear the graph
        histogramView.removeAllSeries();

        // Add the correct data points
        String newYAxis = (String) yAxisSpinnerView.getSelectedItem();
        if (newYAxis.equals("Virus")) {
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
