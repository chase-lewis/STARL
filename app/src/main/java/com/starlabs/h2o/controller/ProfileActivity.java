package com.starlabs.h2o.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.User;

public class ProfileActivity extends AppCompatActivity {


    private EditText nameField;
    private EditText emailField;
    private EditText addressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        nameField = (EditText) findViewById(R.id.user_profile_name_field);
        emailField = (EditText) findViewById(R.id.user_profile_email);
        addressField = (EditText) findViewById(R.id.user_profile_address);
    }
}
