package com.starlabs.h2o.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Initial screen that the user starts on.
 *
 * @author tejun
 */
import com.starlabs.h2o.R;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_initial);

        // Set up the login button
        Button goToLogin = (Button) findViewById(R.id.initial_sign_in);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchLogin();
            }
        });

        // Set up the register button
        Button goToRegister = (Button) findViewById(R.id.initial_register);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchRegister();
            }
        });
    }

    /**
     * Launches the Login activity
     */
    private void launchLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the Register activity
     */
    private void launchRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
