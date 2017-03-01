package com.starlabs.h2o.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starlabs.h2o.R;
import com.starlabs.h2o.model.WaterReport;

import java.util.List;

public class ViewReportsActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private RVAdapter adapter;
    private List<WaterReport> waterReports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        //TODO: Obtain list of Water Reports and put it in waterReports
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("waterReports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numReports = 0;
                for (DataSnapshot report : dataSnapshot.getChildren()) {
                    waterReports.add(report.getValue(WaterReport.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });

        //adapter = new RVAdapter(waterReports);
        mRecycler.setAdapter(adapter);

    }
}
