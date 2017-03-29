package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.starlabs.h2o.R;

import com.starlabs.h2o.controller.water_report.CreateWaterReportFragment;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Rishi on 3/27/2017.
 */

public class SetupHistogramFragment extends Fragment {
    private Spinner yAxisSpinner;
    private EditText yearText;
    private EditText reportNum;
    private WaterReport selectedReport;
    private String spinnerVal;
    private FragmentManager fragmentManager = getFragmentManager();
    
    public SetupHistogramFragment(){
        //required empty constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_histogram, container, false);

        yAxisSpinner = (Spinner) view.findViewById(R.id.histogram_yaxis_choice);
        yearText = (EditText) view.findViewById(R.id.year_histogram_text);
        reportNum = (EditText) view.findViewById(R.id.report_number);
        
        List<String> yaxisChoices = new ArrayList<>();
        yaxisChoices.add("Virus");
        yaxisChoices.add("Contaminant");
        ArrayAdapter<String> yaxisAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, yaxisChoices);
        yaxisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yAxisSpinner.setAdapter(yaxisAdapter);



        // Create button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.view_histogram_button);
        reportCreateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO transfer data to next activity with intent
                onHistogramViewPressed();
            }
        });

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.cancel_histogram_button);
        reportCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCancelPressed(view);
            }
        });
        
        return view;
    }

    protected void onHistogramViewPressed() {
        //check if year is valid input
        int selectYear;

        int reportNumHist;


        try{
            selectYear = Integer.parseInt(yearText.getText().toString());
        }catch(NumberFormatException e){
            yearText.setError("Must pass in a valid number");
            return;
        }
        if(selectYear > 3000 || selectYear < 1900){
            yearText.setError("Year is out of range");
            return;
        }

        try{
            reportNumHist = Integer.parseInt(reportNum.getText().toString());
        }catch(NumberFormatException e){
            yearText.setError("Must pass in a valid number");
            return;
        }

        // Obtain list of Water Reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<WaterReport>> onWaterReportsReceived = new Consumer<List<WaterReport>>() {
            @Override
            public void accept(List<WaterReport> waterReports) {
                for(WaterReport report : waterReports){
                    if(report.getReportNumber() == reportNumHist){
                        selectedReport = report;
                    }
                    if (selectedReport != null){
                        spinnerVal = yAxisSpinner.getSelectedItem().toString();

                        Bundle args = new Bundle();
                        args.putInt("select_year",selectYear);
                        args.putString("spinner_val",spinnerVal);
                        List<Integer> purityReportIds = new ArrayList<>();
                        purityReportIds = selectedReport.getLinkedPurityReports();
                        args.putIntegerArrayList("purity_report_ids", (ArrayList<Integer>) purityReportIds);
                        ((HomeActivity) getActivity()).switchToHistogram(args);
                    }
                }
            }
        };
        contentProvider.getAllWaterReports(onWaterReportsReceived);



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
