package com.starlabs.h2o.controller.water_report;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterCondition;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.report.WaterType;
import com.starlabs.h2o.model.user.User;

import java.util.Date;
import java.util.function.Consumer;

/**
 * A fragment for creating water reports
 *
 * @author tejun, chase
 */
public class CreateWaterReportFragment extends Fragment {

    private final int LAT_BOUND = 90;
    private final int LONG_BOUND = 180;

    // Views
    private TextView reportNumText;
    private TextView reportDateText;
    private TextView userNameText;
    private ImageView userPictureImage;
    private EditText reportLocLatEditText;
    private EditText reportLocLongEditText;
    private Spinner waterTypeSpinner;
    private Spinner waterCondSpinner;

    // Member variables
    private WaterReport report;
    private ContentProvider contentProvider;

    /**
     * Default constructor with no args
     */
    public CreateWaterReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_water_report, container, false);

        // Get the content provider
        contentProvider = ContentProviderFactory.getDefaultContentProvider();

        // Set up the fields for the user profile
        reportNumText = (TextView) view.findViewById(R.id.create_water_report_num);
        reportDateText = (TextView) view.findViewById(R.id.create_water_report_date);
        userNameText = (TextView) view.findViewById(R.id.create_water_report_username);
        userPictureImage = (ImageView) view.findViewById(R.id.create_water_report_profile_picture);
        reportLocLatEditText = (EditText) view.findViewById(R.id.create_water_report_lat);
        reportLocLongEditText = (EditText) view.findViewById(R.id.create_water_report_long);
        waterTypeSpinner = (Spinner) view.findViewById(R.id.create_water_report_type);
        waterCondSpinner = (Spinner) view.findViewById(R.id.create_water_report_condition);

        // Create a new report with the correct values
        User currentUser = contentProvider.getLoggedInUser();
        report = new WaterReport(currentUser.getUsername(), new Location("H20"));

        // Get the correct id for the new report from the content provider
        Consumer<Integer> onNextIdFound = id -> {
            // Set the report number
            report.setReportNumber(id + 1);
            reportNumText.setText("Report Number: " + Integer.toString(report.getReportNumber()));
        };
        contentProvider.getNextWaterReportId(onNextIdFound);

        // Check if report's latLong is being generated due to Map Tap or user's location
        Bundle bundle = getArguments();
        if (bundle != null) {
            LatLng latLng = bundle.getParcelable("LOC");
            if (latLng != null) {
                report.setLatitude(latLng.latitude);
                report.setLongitude(latLng.longitude);
            }
        } else {
            // Set up the location of the report
            // Check if we have location access permission first.
            // Note we are using Network Location, not GPS
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Activity act = getActivity();
                LocationManager locationManager = (LocationManager)
                        act.getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.NETWORK_PROVIDER;

                // Get rough location very synchronously
                Location lastKnownLocation =
                        locationManager.getLastKnownLocation(locationProvider);

                if (lastKnownLocation != null) {
                    // Set the location in the plain old java object
                    report.setLatitude(lastKnownLocation.getLatitude());
                    report.setLongitude(lastKnownLocation.getLongitude());
                }
            }
        }

        // Set all the text views' content
        reportNumText.setText("Report Number: " + Integer.toString(report.getReportNumber()));
        Date date = report.getCreationDate();
        reportDateText.setText("Created On: " + date.toString());
        userNameText.setText("Created By: " + currentUser.getUsername());
        if (currentUser.getProfilePicture() != null) {
            userPictureImage.setImageBitmap(currentUser.getProfilePicture());
        }
        reportLocLatEditText.setText(Double.toString(report.getLatitude()));
        reportLocLongEditText.setText(Double.toString(report.getLongitude()));
        WaterType wType = report.getType();
        waterTypeSpinner.setSelection(wType.ordinal());
        WaterCondition wCondition = report.getCondition();
        waterCondSpinner.setSelection(wCondition.ordinal());

        // Set up spinner content
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, WaterType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterTypeSpinner.setAdapter(typeAdapter);
        ArrayAdapter<String> condAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, WaterCondition.values());
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterCondSpinner.setAdapter(condAdapter);

        // Create button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.create_water_report_create);
        reportCreateButton.setOnClickListener((view2) -> onReportCreatePressed());

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.create_water_report_cancel);
        reportCancelButton.setOnClickListener((view1) -> onCancelPressed());

        return view;
    }

    /**
     * Method to create/finalize edit on report
     */
    private void onReportCreatePressed() {
        // Update the values in the model from the UI
        report.setType((WaterType) waterTypeSpinner.getSelectedItem());
        report.setCondition((WaterCondition) waterCondSpinner.getSelectedItem());

        // Verify the location data is valid
        double latitude;
        double longitude;

        reportLocLatEditText.setError(null);
        reportLocLongEditText.setError(null);

        try {
            Editable locLacText = reportLocLatEditText.getText();
            latitude = Double.parseDouble(locLacText.toString());
        } catch (NumberFormatException e) {
            reportLocLatEditText.setError("Must pass in a valid number");
            return;
        }

        try {
            Editable locLongText = reportLocLongEditText.getText();
            longitude = Double.parseDouble(locLongText.toString());
        } catch (NumberFormatException e) {
            reportLocLongEditText.setError("Must pass in a valid number");
            return;
        }

        if ((-latitude > LAT_BOUND) || (latitude > LAT_BOUND)) {
            reportLocLatEditText.setError("Latitude must be between -90 and 90");
            return;
        } else if ((-longitude > LONG_BOUND) || (longitude > LONG_BOUND)) {
            reportLocLongEditText.setError("Longitude must be between -180 and 180");
            return;
        } else {
            report.setLatitude(latitude);
            report.setLongitude(longitude);
        }

        // Store data
        contentProvider.setWaterReport(report);

        // Increment the next report id if a new one was created
        contentProvider.setNextWaterReportId(report.getReportNumber());

        // Go back
        Activity activity = getActivity();
        activity.onBackPressed();
    }

    /**
     * Method to exit the activity back to caller.
     */
    private void onCancelPressed() {
        Activity active = getActivity();
        active.onBackPressed();
    }
}
