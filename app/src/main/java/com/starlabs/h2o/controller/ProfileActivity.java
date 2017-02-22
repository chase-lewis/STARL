package com.starlabs.h2o.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.starlabs.h2o.R;
import com.starlabs.h2o.model.User;

public class ProfileActivity extends AppCompatActivity {

    public static final String TO_MAIN = "TO_MAIN";

    private EditText nameField;
    private EditText emailField;
    private EditText addressField;


    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        nameField = (EditText) findViewById(R.id.user_profile_name_field);
        emailField = (EditText) findViewById(R.id.user_profile_email);
        addressField = (EditText) findViewById(R.id.user_profile_address);

        //Not Sure if we need to check if there is a parcelable object, since the user should be created by now
//        if (getIntent().hasExtra(RegisterActivity.REG_INTENT)) {
        user = (User) getIntent().getParcelableExtra(RegisterActivity.REG_INTENT);
//        } else {
//
//        }

        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());

        Button profileDoneButton = (Button) findViewById(R.id.profile_done_button);
        profileDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onProfileDonePressed(view);
            }
        });

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
        Log.d("Edit", "Add User Profile");

        user.setName(nameField.getText().toString());
        user.setEmail(emailField.getText().toString());
        user.setAddress(addressField.getText().toString());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUsername()).setValue(user);
        if (getIntent().getBooleanExtra(TO_MAIN, false)) {
            Intent toMain = new Intent(ProfileActivity.this, MainActivity.class);
            toMain.putExtra(LoginActivity.LOG_INTENT, user);
            startActivity(toMain);
        } else {
            Intent result = new Intent();
            result.putExtra(MainActivity.PROF_UPDATE, user);
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
        Log.d("Edit", "Cancel User Profile");
        finish();
    }

}


