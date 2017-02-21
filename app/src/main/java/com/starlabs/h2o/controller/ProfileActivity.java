package com.starlabs.h2o.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.Model;
import com.starlabs.h2o.model.User;

public class ProfileActivity extends AppCompatActivity {

    private User user = Model.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
