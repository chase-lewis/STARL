package com.starlabs.h2o.controller.purity_report;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * @author tejun, chase
 */
public class CreatePurityReportFragment extends Fragment {

    // Views
    private TextView reportNumText;
    private TextView reportDateText;
    private TextView userNameText;
    private ImageView userPictureImage;
    private EditText linkedWaterReportEditText;
    private EditText virusPPMEditText;
    private EditText contPPMEditText;
    private Spinner purityCondSpinner;

    // Member variables
    private PurityReport report;
    private Runnable onFinish = this::onCancelPressed;

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

        // Get the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();

        // Set up the fields for the user profile
        reportDateText = (TextView) view.findViewById(R.id.create_purity_report_date);
        reportNumText = (TextView) view.findViewById(R.id.create_purity_report_num);
        userNameText = (TextView) view.findViewById(R.id.create_purity_report_username);
        userPictureImage = (ImageView) view.findViewById(R.id.create_purity_report_profile_picture);
        linkedWaterReportEditText = (EditText) view.findViewById(R.id.create_purity_linked_water_report);
        virusPPMEditText = (EditText) view.findViewById(R.id.create_purity_report_virus_ppm);
        contPPMEditText = (EditText) view.findViewById(R.id.create_purity_report_cont_ppm);
        purityCondSpinner = (Spinner) view.findViewById(R.id.create_purity_report_condition);

        // Create a new report
        User currentUser = contentProvider.getLoggedInUser();
        report = new PurityReport(currentUser.getUsername());

        // Get the correct id for the new report from the content provider
        Consumer<Integer> onNextIdFound = id -> {
            // Set the report number
            report.setReportNumber(id + 1);
            reportNumText.setText(Integer.toString(report.getReportNumber()));
        };
        contentProvider.getNextPurityReportId(onNextIdFound);

        // Set all the text views
        reportNumText.setText("Report Number: " + Integer.toString(report.getReportNumber()));
        Date date = report.getCreationDate();
        reportDateText.setText(date.toString());
        userNameText.setText("Created By: " + currentUser.getUsername());
        if (currentUser.getProfilePicture() != null) {
            userPictureImage.setImageBitmap(User.stringToBitmap(currentUser.getProfilePicture()));
        }
        linkedWaterReportEditText.setText(Integer.toString(report.getLinkedWaterReportId()));
        virusPPMEditText.setText(report.getVirusPPM() + "");
        contPPMEditText.setText(report.getContPPM() + "");
        purityCondSpinner.setSelection(report.getCondition().ordinal());

        // Set up spinner
        ArrayAdapter<String> condAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, PurityCondition.values());
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purityCondSpinner.setAdapter(condAdapter);

        // Create button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.create_purity_report_create);
        reportCreateButton.setOnClickListener((view2) -> onReportCreatePressed());

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.create_purity_report_cancel);
        reportCancelButton.setOnClickListener((view1) -> onCancelPressed());

        return view;
    }

    /**
     * Method to create/finalize edit on report
     */
    private void onReportCreatePressed() {
        final int linkedWaterReportId = Integer.parseInt(linkedWaterReportEditText.
                getText().toString());

        // Update the values in the model from the UI
        report.setCondition((PurityCondition) purityCondSpinner.getSelectedItem());
        report.setVirusPPM(Integer.parseInt(virusPPMEditText.getText().toString()));
        report.setContPPM(Integer.parseInt(contPPMEditText.getText().toString()));
        report.setLinkedWaterReportId(linkedWaterReportId);

        // Store purity report
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.setPurityReport(report);
        contentProvider.setNextPurityReportId(report.getReportNumber());

        // Store association in the water report
        ReportManager reportManager = ReportManager.getInstance(contentProvider);
        reportManager.linkPurityReport(report.getReportNumber(), linkedWaterReportId, onFinish);

        // Note that the activity exits in the callback above
    }

    /**
     * Method to exit the activity back to caller.
     */
    private void onCancelPressed() {
        getActivity().onBackPressed();
    }

}
