package com.starlabs.h2o.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.starlabs.h2o.R;
import com.starlabs.h2o.model.user.User;

/**
 * Activity to edit the user profile
 *
 * @author tejun
 */
public class ProfileActivity extends AppCompatActivity {

    // Intent message ids
    public static final String TO_MAIN = "TO_MAIN";
    public static final String PROF_UPDATE = "PROF UPDATE";
    // Field views
    private EditText nameField;
    private EditText emailField;
    private EditText addressField;

    // User passed into this activity
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up the fields for the user profile
        nameField = (EditText) findViewById(R.id.user_profile_name_field);
        emailField = (EditText) findViewById(R.id.user_profile_email);
        addressField = (EditText) findViewById(R.id.user_profile_address);

        // Get the user from the intent message
        user = getIntent().getParcelableExtra(RegisterActivity.REG_INTENT);

        // Set up the text pre-defined values
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());

        nameField.requestFocus();

        // Done button setup
        Button profileDoneButton = (Button) findViewById(R.id.profile_done_button);
        profileDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onProfileDonePressed(view);
            }
        });

        // Cancel button setup
        Button profileCancelButton = (Button) findViewById(R.id.profile_cancel_button);
        profileCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCancelPressed(view);
            }
        });
    }

    /**
     * Method to exit the activity to main.
     *
     * @param view the parameter View
     */
    protected void onProfileDonePressed(View view) {
        // Update the user model from the fields
        user.setName(nameField.getText().toString());
        user.setEmail(emailField.getText().toString());
        user.setAddress(addressField.getText().toString());

        // Store the user in firebase, overwriting any values already there
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUsername()).setValue(user);

        // Transition to the proper activity
        if (getIntent().getBooleanExtra(TO_MAIN, false)) {
            Intent toMain = new Intent(ProfileActivity.this, MainActivity.class);
            toMain.putExtra(MainActivity.USER_TO_MAIN, user);
            startActivity(toMain);
        } else {
            // TODO
            Intent result = new Intent();
            result.putExtra(PROF_UPDATE, user);
            setResult(Activity.RESULT_OK, result);
        }
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


