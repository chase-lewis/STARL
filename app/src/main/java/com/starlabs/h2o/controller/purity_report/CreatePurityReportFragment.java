package com.starlabs.h2o.controller.purity_report;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.facade.ReportManager;
import com.starlabs.h2o.model.report.PurityCondition;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.user.User;

import java.util.Date;
import java.util.function.Consumer;

/**
 * A fragment for creating purity reports
 *
 * @author chase
 */
public class CreatePurityReportFragment extends Fragment {

    private TextView reportNumText;
    private EditText linkedWaterReportEditText;
    private Spinner purityCondSpinner;
    private EditText virusPPMText;
    private EditText contPPMText;
    private PurityReport report;

    /**
     * Default constructor with no args
     */
    public CreatePurityReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_purity_report, container, false);

        // Set up the fields for the user profile
        TextView reportDateText = (TextView) view.findViewById(R.id.create_purity_report_date);
        reportNumText = (TextView) view.findViewById(R.id.create_purity_report_num);
        linkedWaterReportEditText = (EditText)
                view.findViewById(R.id.create_purity_linked_water_report);
        TextView reportReporterName = (TextView)
                view.findViewById(R.id.create_purity_report_username);
        purityCondSpinner = (Spinner) view.findViewById(R.id.create_purity_report_condition);
        virusPPMText = (EditText) view.findViewById(R.id.create_purity_report_virus_ppm);
        contPPMText = (EditText) view.findViewById(R.id.create_purity_report_cont_ppm);

        // Get the user from session
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        User user = contentProvider.getLoggedInUser();

        ArrayAdapter<String> condAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, PurityCondition.values());
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purityCondSpinner.setAdapter(condAdapter);

        bundleItUp(user, contentProvider);

        // Set all the text views
        reportReporterName.setText(user.getName());
        //noinspection ChainedMethodCall
        Date date = report.getCreationDate();
        reportDateText.setText(date.toString());
        reportNumText.setText(Integer.toString(report.getReportNumber()));
        linkedWaterReportEditText.setText(Integer.toString(report.getLinkedWaterReportId()));
        //noinspection ChainedMethodCall
        purityCondSpinner.setSelection(report.getCondition().ordinal());
        virusPPMText.setText(report.getVirusPPM() + "");
        contPPMText.setText(report.getContPPM() + "");

        // Create button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.create_purity_report_create);
        reportCreateButton.setOnClickListener((view2) -> onReportCreatePressed());

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.create_purity_report_cancel);
        reportCancelButton.setOnClickListener((view1) -> onCancelPressed());

        return view;
    }

    /**
     * Creates a bundle for fragment
     *
     * @param user            user creating report
     * @param contentProvider firebase object
     */
    public void bundleItUp(User user, ContentProvider contentProvider) {
        Bundle bundle = getArguments();
        if ((bundle != null) && (bundle.getParcelable("PR_EDIT") != null)) {
            report = bundle.getParcelable("PR_EDIT");
        } else {
            // Create a new report
            report = new PurityReport(user.getName());

            // Get the correct id for the new report from the content provider
            Consumer<Integer> onNextIdFound = id -> {
                // Set the report number
                report.setReportNumber(id + 1);
                reportNumText.setText(Integer.toString(report.getReportNumber()));
            };
            contentProvider.getNextPurityReportId(onNextIdFound);
        }
    }

    /**
     * Method to create/finalize edit on report
     */
    private void onReportCreatePressed() {
        final int linkedWaterReportId = Integer.parseInt(linkedWaterReportEditText.
                getText().toString());

        // Update the values in the model from the UI
        report.setCondition((PurityCondition) purityCondSpinner.getSelectedItem());
        //noinspection ChainedMethodCall
        report.setVirusPPM(Integer.parseInt(virusPPMText.getText().toString()));
        //noinspection ChainedMethodCall
        report.setContPPM(Integer.parseInt(contPPMText.getText().toString()));
        report.setLinkedWaterReportId(linkedWaterReportId);

        // Store purity report
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.setPurityReport(report);
        contentProvider.setNextPurityReportId(report.getReportNumber());

        // Store association in the water report
        Runnable onFinish = () -> {
            // Exit this fragment here
            //noinspection ChainedMethodCall
            getActivity().onBackPressed();
        };
        ReportManager reportManager = ReportManager.getInstance(contentProvider);
        reportManager.linkPurityReport(report.getReportNumber(), linkedWaterReportId, onFinish);

        // Note that the activity exits in the callback above
    }

    /**
     * Method to exit the activity back to caller.
     */
    private void onCancelPressed() {
        //noinspection ChainedMethodCall
        getActivity().onBackPressed();
    }

}
