package com.starlabs.h2o.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by tejun on 3/8/2017.
 */

class FirebaseContentProvider implements ContentProvider {

    private DatabaseReference mDatabase;

    FirebaseContentProvider() {
        // Firebase database authentication
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAllUsers(Consumer<List<User>> callback) {
        // TODO
    }

    @Override
    public void getSingleUser(Consumer<User> callback, String username) {
        // Create a listener for specific username
        mDatabase.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // Found a user with a matching username
                    // Extract out the user object from firebase
                    User user = dataSnapshot.getValue(User.class);

                    // Call the provided callback
                    callback.accept(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing yet
            }
        });
    }

    @Override
    public void setUser(User user) {
        mDatabase.child("users").child(user.getUsername()).setValue(user);
    }

    @Override
    public void getAllWaterReports(Consumer<List<WaterReport>> callback) {
        mDatabase.child("waterReports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<WaterReport> waterReports = new ArrayList<WaterReport>();

                for (DataSnapshot report : dataSnapshot.getChildren()) {
                    waterReports.add(report.getValue(WaterReport.class));
                }

                callback.accept(waterReports);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    @Override
    public void getSingleWaterReport(Consumer<WaterReport> callback, int id) {
        // TODO
    }

    @Override
    public void setWaterReport(WaterReport waterReport) {
        mDatabase.child("waterReports").child("" + waterReport.getReportNumber()).setValue(waterReport);
    }

    @Override
    public void getNextWaterReportId(Consumer<Integer> callback) {
        // Create a listener for the next report id
        mDatabase.child("waterReportId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int nextId = dataSnapshot.getValue(Integer.class);

                callback.accept(nextId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    @Override
    public void setNextWaterReportId(int nextWaterReportId) {
        mDatabase.child("waterReportId").setValue(nextWaterReportId);
    }
}
