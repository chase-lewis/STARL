package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.*;
import com.starlabs.h2o.R;

import com.starlabs.h2o.controller.water_report.CreateWaterReportFragment;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;

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
    private List<PurityReport> purReports;;

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
        String initYAxis = (String)b.getString("spinner_val");
        int reportYear = (int)b.getInt("select_year");
        List<Integer> purityReportIds = b.getIntegerArrayList("purity_report_ids");
        // Obtain list of Water Purity Reports from the content provider

        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<PurityReport>> waterReportsReceived = new Consumer<List<PurityReport>>() {
            @Override
            public void accept(List<PurityReport> waterPurityReports) {
                purReports = new ArrayList<>();
                for(PurityReport preport : waterPurityReports){
                    boolean reportFound = false;
                    int i = 0;
                    while(!reportFound && i < purityReportIds.size()){
                        if(preport.getReportNumber() == purityReportIds.get(i)){
                            reportFound = true;
                            purReports.add(preport);
                            i++;
                        }
                    }
                }

                yAxisSpinner.setSelection(yaxisChoices.indexOf(initYAxis));

                //Debugging
                Log.d("purityreportsize",Integer.toString(purReports.size()));
                Log.d("idsSIZE",Integer.toString(purityReportIds.size()));
                //Log.d("purityreport",purReports.get(0).getworkerName());

                //Get whether user wants to see contamination or virus level
                String yaxis = (String)yAxisSpinner.getSelectedItem();

                //Set Title based on y axis
                String titleToShow = reportYear + " " + yaxis + " Histogram";
                histogramTitle = (TextView) view.findViewById(R.id.histogramReportTitle);
                histogramTitle.setText(titleToShow);


                GraphView histogram = (GraphView) view.findViewById(R.id.histogramGraph);
                histogram.getGridLabelRenderer().setHorizontalAxisTitle("Purity Report ID");
                histogram.removeAllSeries();
                if (yaxis == "Virus") {
                    //TODO: GRAPH POINTS BASE ON WHAT RISHI PASSES IN
                } else {
                    //TODO: GRAPH POINTS BASE ON WHAT RISHI PASSES IN
                }

                Button updateHistogramButton = (Button) view.findViewById(R.id.updateHistogramButton);
                updateHistogramButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String newYaxis = (String)yAxisSpinner.getSelectedItem();
                        histogram.removeAllSeries();
                        if (newYaxis == "Virus") {
                            //TODO: GRAPH POINTS BASE ON WHAT RISHI PASSES IN
                        } else {
                            //TODO: GRAPH POINTS BASE ON WHAT RISHI PASSES IN
                        }
                        String titleToShow = reportYear + " " + newYaxis + " Histogram";
                        histogramTitle.setText(titleToShow);
                    }
                });

                // Cancel button setup
                Button reportCancelButton = (Button) view.findViewById(R.id.histogramCancel);
                reportCancelButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        onCancelPressed(view);
                    }
                });
            }
        };
        contentProvider.getAllPurityReports(waterReportsReceived);
        return view;
    }

    protected void getPurityReports(List<Integer> purityReportIds){

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
