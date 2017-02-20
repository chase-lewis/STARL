package com.starlabs.h2o.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.starlabs.h2o.R;

/**
 * Main screen a user sees after logging in
 *
 * @author tejun
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_main);

        // Set up a temporary return button
        Button tempReturn = (Button) findViewById(R.id.main_temp_return);
        tempReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
