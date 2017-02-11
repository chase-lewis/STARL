package com.starlabs.h2o;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams
                    .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color
                    .colorPrimaryDark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color
                    .colorPrimaryDark));
        }
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
