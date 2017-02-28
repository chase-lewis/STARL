package com.starlabs.h2o.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.Person;

/**
 * Main screen a user sees after logging in
 *
 * @author tejun
 */
public class MainActivity extends AppCompatActivity {
    public static final String USER_TO_MAIN = "USER_TO_MAIN";
    public static final String MAIN_TO_REPORT_USER = "USER";
    public static final String MAIN_TO_REPORT_REPORT = "REPORT";
    private final int CODE = 392;
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getIntent().getParcelableExtra(USER_TO_MAIN);

        // Set the layout
        setContentView(R.layout.activity_main);

        // Set up a temporary return button
        Button tempReturn = (Button) findViewById(R.id.main_temp_return);
        tempReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        Button profileEdit = (Button) findViewById(R.id.main_profile_edit);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra(RegisterActivity.REG_INTENT, user);
                startActivityForResult(profileIntent, CODE);
            }
        });

    }

    /**
     * Method called when startActivityForResult returns
     *
     * @param requestCode the code indicating the original caller
     * @param resultCode  the code indicating whether the return was successful
     * @param data        the data returned
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE && resultCode == RESULT_OK) {
            user = data.getParcelableExtra(ProfileActivity.PROF_UPDATE);
        }
    }
}
