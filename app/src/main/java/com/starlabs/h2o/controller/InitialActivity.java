package com.starlabs.h2o.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.starlabs.h2o.R;
import com.starlabs.h2o.controller.user.LoginUserActivity;
import com.starlabs.h2o.controller.user.RegisterUserActivity;

/**
 * Initial screen that the user starts on.
 *
 * @author tejun, chase
 */

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_initial);

        // Set up the login button
        Button goToLogin = (Button) findViewById(R.id.initial_sign_in);
        goToLogin.setOnClickListener(view -> launchLogin());

        // Set up the register button
        Button goToRegister = (Button) findViewById(R.id.initial_register);
        goToRegister.setOnClickListener(view -> launchRegister());

        final int PERM = 39;
        // Get user location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERM);
        }
    }

    /**
     * Launches the Login activity
     */
    private void launchLogin() {
        Intent intent = new Intent(this, LoginUserActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the Register activity
     */
    private void launchRegister() {
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }
}
