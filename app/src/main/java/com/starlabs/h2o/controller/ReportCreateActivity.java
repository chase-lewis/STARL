package com.starlabs.h2o.controller;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starlabs.h2o.R;
import com.starlabs.h2o.model.WaterCondition;
import com.starlabs.h2o.model.WaterReport;
import com.starlabs.h2o.model.WaterType;
import com.starlabs.h2o.model.user.Person;

import java.util.Date;


/**
 * Activity to create Water Report
 *
 * @author Kavin Krishnan
 */
public class ReportCreateActivity extends AppCompatActivity {

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
    private Person user;

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

            // Change all the values that need to be updated now
            report.setCreationDate(new Date());
            report.setReporterName(user.getName());
            // TODO set the new location

        } else {
            // Create a new report
            report = new WaterReport(user.getName(), new Location("H20"), WaterType.BOTTLED, WaterCondition.POTABLE);

            // Firebase database for getting the number of reports
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            // Create a listener for specific username
            mDatabase.child("waterReportId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int numReports = dataSnapshot.getValue(Integer.class);

                    // Set the report number
                    report.setReportNumber(numReports + 1);
                    reportNumText.setText(Integer.toString(report.getReportNumber()));
                    mDatabase.child("waterReportId").setValue(numReports + 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Do nothing
                }
            });

            // TODO: Teju and Chase fix this with location
            // erase this line and make it update with GPS
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
        // TODO verify that the values in the fields are correct

        // Update the values in the model from the UI
        report.setType((WaterType) waterTypeSpinner.getSelectedItem());
        report.setCondition((WaterCondition) waterCondSpinner.getSelectedItem());
        // TODO set the location
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

        // Store data in firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("waterReports").child("" + report.getReportNumber()).setValue(report);

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


