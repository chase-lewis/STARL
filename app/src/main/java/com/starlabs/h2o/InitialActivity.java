package com.starlabs.h2o;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        Button goToLogin = (Button) findViewById(R.id.initial_sign_in);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchLogin();
            }
        });

        Button goToRegister = (Button) findViewById(R.id.initial_register);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchRegister();
            }
        });

    }

    private void launchLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void launchRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
