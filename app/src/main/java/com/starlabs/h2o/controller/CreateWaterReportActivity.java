package com.starlabs.h2o.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterCondition;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.report.WaterType;
import com.starlabs.h2o.model.user.User;

import java.util.function.Consumer;


/**
 * Activity to create Water Report
 *
 * @author Kavin Krishnan
 */
public class CreateWaterReportActivity extends AppCompatActivity {

    // Intent message ids
    public static final String TO_REPORT_USER = "TO_WATER_REPORT";
    public static final String WATER_REPORT_TO_REPORT = "WRTR";

    // Field views
    private TextView reportDateText;
    private TextView reportNumText;
    private EditText reportLocLatEditText;
    private EditText reportLocLongEditText;
    private TextView reportReporterName;
    private Spinner waterTypeSpinner;
    private Spinner waterCondSpinner;

    // User passed into this activity
    private User user;

    // Report potentially being passed in
    private WaterReport report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportcreate);

        // Set up the fields for the user profile
        reportDateText = (TextView) findViewById(R.id.reportDate);
        reportNumText = (TextView) findViewById(R.id.reportNum);
        reportLocLatEditText = (EditText) findViewById(R.id.reportLocationLat);
        reportLocLongEditText = (EditText) findViewById(R.id.reportLocationLong);
        reportReporterName = (TextView) findViewById(R.id.reportReportName);
        waterTypeSpinner = (Spinner) findViewById(R.id.WaterTypeSpin);
        waterCondSpinner = (Spinner) findViewById(R.id.WaterCondSpin);

        // Get the user from the intent message
        user = getIntent().getParcelableExtra(TO_REPORT_USER);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, WaterType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> condAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, WaterCondition.values());
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterCondSpinner.setAdapter(condAdapter);

        // TODO: Someone remember to send a parcel from main class when editing
        if (getIntent().hasExtra(WATER_REPORT_TO_REPORT)) {
            // TODO get the report from the intent
            // TODO do we need to change any values? I don't think so, just let the user update them
        } else {
            // Create a new report
            report = new WaterReport(user.getName(), new Location("H20"), WaterType.BOTTLED, WaterCondition.POTABLE);

            // Get the correct id for the new report from the content provider
            ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
            Consumer<Integer> onNextIdFound = new Consumer<Integer>() {
                @Override
                public void accept(Integer id) {
                    // Set the report number
                    report.setReportNumber(id + 1);
                    reportNumText.setText(Integer.toString(report.getReportNumber()));

                    // Incremement next id in the content provider
                    contentProvider.setNextWaterReportId(id + 1);
                }
            };
            contentProvider.getNextWaterReportId(onNextIdFound);

            // Check if report's latLong is being generated due to Map Tap or user's location
            if (getIntent().hasExtra("fromMapClick")) {
                report.setLatitude(getIntent().getDoubleExtra("latitude", 0));
                report.setLongitude(getIntent().getDoubleExtra("longitude", 0));
            } else {
                // Set up the location of the report
                // Check if we have location access permission first. Note we are using Network Location, not GPS
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
        Button reportCreateButton = (Button) findViewById(R.id.reportCreateButton);
        reportCreateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onReportCreatePressed(view);
            }
        });

        // Cancel button setup
        Button reportCancelButton = (Button) findViewById(R.id.reportCancelButton);
        reportCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCancelPressed(view);
            }
        });
    }

    /**
     * Method to create/finalize edit on report
     *
     * @param view the parameter View
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

        finish();
    }

    /**
     * Method to exit the activity back to caller.
     *
     * @param view the parameter View
     */
    protected void onCancelPressed(View view) {
        finish();
    }

}


