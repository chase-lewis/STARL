package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.starlabs.h2o.R;
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

        // Inflate views
        yAxisSpinner = (Spinner) view.findViewById(R.id.histogram_yaxis_choice);
        yearText = (EditText) view.findViewById(R.id.year_histogram_text);
        reportNum = (EditText) view.findViewById(R.id.report_number);

        // Setup y axis spinner
        List<String> yaxisChoices = new ArrayList<>();
        yaxisChoices.add("Virus");
        yaxisChoices.add("Contaminant");
        ArrayAdapter<String> yaxisAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, yaxisChoices);
        yaxisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yAxisSpinner.setAdapter(yaxisAdapter);

        // View button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.view_histogram_button);
        reportCreateButton.setOnClickListener(view1 -> onHistogramViewPressed());

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.cancel_histogram_button);
        reportCancelButton.setOnClickListener(this::onCancelPressed);
        
        return view;
    }

    protected void onHistogramViewPressed() {
        // Get input from ui
        String spinnerVal = yAxisSpinner.getSelectedItem().toString();
        int selectYear;
        int reportNumHist;

        try {
            selectYear = Integer.parseInt(yearText.getText().toString());
        } catch (NumberFormatException e){
            yearText.setError("Must pass in a valid number");
            return;
        }

        if(selectYear > 3000 || selectYear < 1900){
            yearText.setError("Year is out of range");
            return;
        }

        try {
            reportNumHist = Integer.parseInt(reportNum.getText().toString());
        } catch(NumberFormatException e){
            reportNum.setError("Must pass in a valid number");
            return;
        }

        // Obtain list of Water Reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<WaterReport> onWaterReportReceived = waterReport -> {
            if (waterReport != null) {
                // Transition to the histogram fragment
                Bundle args = new Bundle();
                args.putInt("select_year", selectYear);
                args.putString("spinner_val", spinnerVal);

                // Place purity ids in the bundle
                List<Integer> purityReportIds = waterReport.getLinkedPurityReports();
                args.putIntegerArrayList("purity_report_ids", (ArrayList<Integer>) purityReportIds);
                ((HomeActivity) getActivity()).switchToHistogram(args);

            } else {
                // No water report found in the db
                reportNum.setError("Not a valid water report number");
            }
        };
        contentProvider.getSingleWaterReport(onWaterReportReceived, reportNumHist);

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
