package com.starlabs.h2o.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.starlabs.h2o.R;
import com.starlabs.h2o.model.Person;
import com.starlabs.h2o.model.WaterReport;

/**
 * Activity to create Water Report
 *
 * @author Kavin Krishnan
 */
public class ReportCreateActivity extends AppCompatActivity {

    // Intent message ids
    public static final String TO_MAIN = "TO_MAIN";
    public static final String PROF_UPDATE = "PROF UPDATE";

    // Field views
    private TextView reportDateText;
    private TextView reportNumText;
    private TextView reportLocField;
    private TextView reportReporterNmae;
    private Spinner waterType;
    private Spinner waterCond;

    // User passed into this activity
    private Person user;

    // Report potentially being passed in
    private WaterReport report;

    // Flag telling if this water report is being edited
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportcreate);

        // Set up the fields for the user profile
        reportDateText = (TextView) findViewById(R.id.reportDate);;
        reportNumText = (TextView) findViewById(R.id.reportNum);;
        reportLocField = (TextView) findViewById(R.id.reportLocation);;
        reportReporterNmae = (TextView) findViewById(R.id.reportReportName);;
        waterType = (Spinner) findViewById(R.id.WaterTypeSpin);;
        waterCond = (Spinner) findViewById(R.id.WaterCondSpin);;

        // Get the user from the intent message
        user = getIntent().getParcelableExtra(MainActivity.MAIN_TO_REPORT_USER);

        // Set up the text pre-defined values
        reportReporterNmae.setText(user.getName());

        if (getIntent().hasExtra(MainActivity.MAIN_TO_REPORT_REPORT)) {
            editing = true;
        } else {
            editing = false;
        }


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
//        // Update the user model from the fields
//        user.setName(nameField.getText().toString());
//        user.setEmail(emailField.getText().toString());
//        user.setAddress(addressField.getText().toString());
//
//        // Store the user in firebase, overwriting any values already there
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child(user.getUsername()).setValue(user);
//
//        // Transition to the proper activity
//        if (getIntent().getBooleanExtra(TO_MAIN, false)) {
//            Intent toMain = new Intent(ProfileActivity.this, MainActivity.class);
//            toMain.putExtra(MainActivity.USER_TO_MAIN, user);
//            startActivity(toMain);
//        } else {
//            // TODO
//            Intent result = new Intent();
//            result.putExtra(PROF_UPDATE, user);
//            setResult(Activity.RESULT_OK, result);
//        }
//        finish();
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


