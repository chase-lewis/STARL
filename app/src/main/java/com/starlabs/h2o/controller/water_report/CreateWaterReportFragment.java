package com.starlabs.h2o.controller.water_report;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.function.Consumer;

/**
 * A fragment for creating water reports
 *
 * @author chase
 */
public class CreateWaterReportFragment extends Fragment {

    User user;
    private boolean edit = false;
    private TextView reportDateText;
    private TextView reportNumText;
    private EditText reportLocLatEditText;
    private EditText reportLocLongEditText;
    private TextView reportReporterName;
    private Spinner waterTypeSpinner;
    private Spinner waterCondSpinner;
    private WaterReport report;

    public CreateWaterReportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_water_report, container, false);

        // Set up the fields for the user profile
        reportDateText = (TextView) view.findViewById(R.id.create_water_report_date);
        reportNumText = (TextView) view.findViewById(R.id.create_water_report_num);
        reportLocLatEditText = (EditText) view.findViewById(R.id.create_water_report_lat);
        reportLocLongEditText = (EditText) view.findViewById(R.id.create_water_report_long);
        reportReporterName = (TextView) view.findViewById(R.id.create_water_report_username);
        waterTypeSpinner = (Spinner) view.findViewById(R.id.create_water_report_type);
        waterCondSpinner = (Spinner) view.findViewById(R.id.create_water_report_condition);

        // Get the user from the session
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        user = contentProvider.getLoggedInUser();

        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, WaterType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> condAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, WaterCondition.values());
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterCondSpinner.setAdapter(condAdapter);

        // TODO: Someone remember to send a parcel from main class when editing
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getParcelable("WR_EDIT") != null) {
            report = bundle.getParcelable("WR_EDIT");
            edit = true;
        } else {
            // Create a new report
            report = new WaterReport(user.getName(), new Location("H20"), WaterType.BOTTLED, WaterCondition.POTABLE);

            // Get the correct id for the new report from the content provider
            Consumer<Integer> onNextIdFound = new Consumer<Integer>() {
                @Override
                public void accept(Integer id) {
                    // Set the report number
                    report.setReportNumber(id + 1);
                    reportNumText.setText(Integer.toString(report.getReportNumber()));
                }
            };
            contentProvider.getNextWaterReportId(onNextIdFound);

            // Check if report's latLong is being generated due to Map Tap or user's location
            if (bundle != null) {
                LatLng latLng = bundle.getParcelable("LOC");
                if (latLng != null) {
                    report.setLatitude(latLng.latitude);
                    report.setLongitude(latLng.longitude);
                }
            } else {
                // Set up the location of the report
                // Check if we have location access permission first. Note we are using Network Location, not GPS
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    String locationProvider = LocationManager.NETWORK_PROVIDER;

                    // Get rough location very synchronously
                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                    if (lastKnownLocation != null) {
                        // Set the location in the pojo
                        report.setLatitude(lastKnownLocation.getLatitude());
                        report.setLongitude(lastKnownLocation.getLongitude());
                    }
                }
            }
        }

        // Set all the text views
        reportReporterName.setText(user.getName());
        reportDateText.setText(report.getCreationDate().toString());
        reportNumText.setText(Integer.toString(report.getReportNumber()));
        reportLocLatEditText.setText(Double.toString(report.getLatitude()));
        reportLocLongEditText.setText(Double.toString(report.getLongitude()));
        waterTypeSpinner.setSelection(report.getType().ordinal());
        waterCondSpinner.setSelection(report.getCondition().ordinal());

        // Create button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.create_water_report_create);
        reportCreateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onReportCreatePressed(view);
            }
        });

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.create_water_report_cancel);
        reportCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCancelPressed(view);
            }
        });
        return view;
    }

    /**
     * Method to create/finalize edit on report
     *
     * @param view the parameter View
     *
     * TODO move business logic out of activity
     */
    protected void onReportCreatePressed(View view) {
        // Update the values in the model from the UI
        report.setType((WaterType) waterTypeSpinner.getSelectedItem());
        report.setCondition((WaterCondition) waterCondSpinner.getSelectedItem());

        // Verify the location data is valid
        double latitude;
        double longitude;

        reportLocLatEditText.setError(null);
        reportLocLongEditText.setError(null);

        try {
            latitude = Double.parseDouble(reportLocLatEditText.getText().toString());
        } catch (NumberFormatException e) {
            reportLocLatEditText.setError("Must pass in a valid number");
            return;
        }

        try {
            longitude = Double.parseDouble(reportLocLongEditText.getText().toString());
        } catch (NumberFormatException e) {
            reportLocLongEditText.setError("Must pass in a valid number");
            return;
        }

        if (latitude < -90 || latitude > 90) {
            reportLocLatEditText.setError("Latitude must be between -90 and 90");
            return;
        } else if (longitude < -180 || longitude > 180) {
            reportLocLongEditText.setError("Longitude must be between -180 and 180");
            return;
        } else {
            report.setLatitude(latitude);
            report.setLongitude(longitude);
        }

        // Store data
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.setWaterReport(report);
        if (!edit) {
            contentProvider.setNextWaterReportId(report.getReportNumber());
        }

        getActivity().onBackPressed();
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
