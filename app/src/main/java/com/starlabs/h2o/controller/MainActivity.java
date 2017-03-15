package com.starlabs.h2o.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.starlabs.h2o.R;
import com.starlabs.h2o.controller.purity_report.CreatePurityReportActivity;
import com.starlabs.h2o.controller.purity_report.ViewPurityReportsActivity;
import com.starlabs.h2o.controller.water_report.CreateWaterReportActivity;
import com.starlabs.h2o.controller.water_report.ViewWaterReportsActivity;
import com.starlabs.h2o.controller.water_report.WaterReportMapActivity;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.user.User;
import com.starlabs.h2o.model.user.UserType;

/**
 * Main screen a user sees after logging in
 *
 * @author tejun
 */
public class MainActivity extends AppCompatActivity {
    public static final String USER_TO_MAIN = "USER_TO_MAIN";
    private final int CODE = 392;
    private User user;

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
                Intent profileIntent = new Intent(MainActivity.this, ViewUserProfileActivity.class);
                profileIntent.putExtra(RegisterUserActivity.REG_INTENT, user);
                startActivityForResult(profileIntent, CODE);
            }
        });

        Button waterReportEdit = (Button) findViewById(R.id.water_report_create);
        waterReportEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, CreateWaterReportActivity.class);
                profileIntent.putExtra(CreateWaterReportActivity.USER_TO_REPORT, user);
                startActivity(profileIntent);
            }
        });

        Button waterReportsView = (Button) findViewById(R.id.water_reports_view);
        waterReportsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportsIntent = new Intent(MainActivity.this, ViewWaterReportsActivity.class);
                startActivity(reportsIntent);
            }
        });

        Button purityReportCreate = (Button) findViewById(R.id.purity_report_create);
        purityReportCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (user.getUserType() != UserType.USER) {
                    Intent profileIntent = new Intent(MainActivity.this, CreatePurityReportActivity.class);
                    profileIntent.putExtra(CreatePurityReportActivity.USER_TO_REPORT, user);
                    startActivity(profileIntent);
                }
            }
        });

        Button waterPurityReportsView = (Button) findViewById(R.id.water_purity_reports_view);
        waterPurityReportsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent purityReportsIntent = new Intent(MainActivity.this, ViewPurityReportsActivity.class);
                startActivity(purityReportsIntent);
            }
        });

        Button waterReportMap = (Button) findViewById(R.id.water_map_view);
        waterReportMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUserType() != UserType.USER && user.getUserType() != UserType.WORKER) {
                    Intent mapIntent = new Intent(MainActivity.this, WaterReportMapActivity.class);
                    mapIntent.putExtra(CreateWaterReportActivity.USER_TO_REPORT, user);
                    startActivity(mapIntent);
                }
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
            user = data.getParcelableExtra(ViewUserProfileActivity.PROF_UPDATE);
        }
    }
}
