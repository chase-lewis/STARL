package com.starlabs.h2o.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Specific type of content provider that uses firebase
 */

class FirebaseContentProvider extends SessionContentProvider implements ContentProvider {

    private DatabaseReference mDatabase;

    FirebaseContentProvider() {
        super();

        // Firebase database authentication
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAllUsers(Consumer<List<User>> callback) {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<User>();

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    users.add(user.getValue(User.class));
                }

                callback.accept(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    @Override
    public void getSingleUser(Consumer<User> callback, String username) {
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
    public void getSingleWaterReport(Consumer<WaterReport> callback, int reportNumber) {
        mDatabase.child("waterReports").child("" + reportNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // Found a user with a matching username
                    // Extract out the user object from firebase
                    WaterReport waterReport = dataSnapshot.getValue(WaterReport.class);

                    // Call the provided callback
                    callback.accept(waterReport);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing yet
            }
        });
    }

    @Override
    public void setWaterReport(WaterReport waterReport) {
        mDatabase.child("waterReports").child("" + waterReport.getReportNumber()).setValue(waterReport);
    }

    @Override
    public void getNextWaterReportId(Consumer<Integer> callback) {
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
    public void setNextWaterReportId(int reportNumber) {
        mDatabase.child("waterReportId").setValue(reportNumber);
    }

    @Override
    public void getAllPurityReports(Consumer<List<PurityReport>> callback) {
        mDatabase.child("purityReports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PurityReport> purityReports = new ArrayList<PurityReport>();

                for (DataSnapshot report : dataSnapshot.getChildren()) {
                    purityReports.add(report.getValue(PurityReport.class));
                }

                callback.accept(purityReports);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    @Override
    public void getSinglePurityReport(Consumer<PurityReport> callback, int reportNumber) {
        mDatabase.child("purityReports").child("" + reportNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // Found a user with a matching username
                    // Extract out the user object from firebase
                    PurityReport purityReport = dataSnapshot.getValue(PurityReport.class);

                    // Call the provided callback
                    callback.accept(purityReport);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing yet
            }
        });
    }

    @Override
    public void setPurityReport(PurityReport purityReport) {
        mDatabase.child("purityReports").child("" + purityReport.getReportNumber()).setValue(purityReport);
    }

    @Override
    public void getNextPurityReportId(Consumer<Integer> callback) {
        mDatabase.child("purityReportId").addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void setNextPurityReportId(int reportNumber) {
        mDatabase.child("purityReportId").setValue(reportNumber);
    }
}
