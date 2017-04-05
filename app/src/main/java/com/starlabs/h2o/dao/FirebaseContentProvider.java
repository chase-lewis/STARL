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
 *
 * @author tejun
 */
class FirebaseContentProvider extends SessionContentProvider implements ContentProvider {

    private final DatabaseReference mDatabase;

    FirebaseContentProvider() {
        super();

        // Firebase database authentication
        FirebaseDatabase dBInstance = FirebaseDatabase.getInstance();
        mDatabase = dBInstance.getReference();
    }

    @Override
    public void getAllUsers(Consumer<List<User>> callback) {
        DatabaseReference databaseRef = mDatabase.child("users");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();

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
        DatabaseReference databaseRef = mDatabase.child("users");
        DatabaseReference databaseRef2 = databaseRef.child(username);
        databaseRef2.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference faceBookDatabase = mDatabase.child("users");
        DatabaseReference faceBookDatabase2 = faceBookDatabase.child(user.getUsername());
        faceBookDatabase2.setValue(user);
    }

    @Override
    public void getAllWaterReports(Consumer<List<WaterReport>> callback) {
        DatabaseReference databaseRef = mDatabase.child("waterReports");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<WaterReport> waterReports = new ArrayList<>();

                for (DataSnapshot report : dataSnapshot.getChildren()) {
                    WaterReport waterReport = report.getValue(WaterReport.class);

                    waterReport.resetLinkedPurityReports();
                    DataSnapshot databasePurReports = report.child("linkedPurityReports");
                    for (DataSnapshot idDs : databasePurReports.getChildren()) {
                        Integer id = idDs.getValue(Integer.class);
                        waterReport.linkPurityReport(id);
                    }

                    waterReports.add(waterReport);
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
        DatabaseReference dataBaseRef = mDatabase.child("waterReports");
        DatabaseReference dataBaseRef2 = dataBaseRef.child("" + reportNumber);
        dataBaseRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // Found a water report with a matching id
                    // Extract out the user object from firebase
                    WaterReport waterReport = dataSnapshot.getValue(WaterReport.class);

                    waterReport.resetLinkedPurityReports();
                    DataSnapshot purReports1 = dataSnapshot.child("linkedPurityReports");
                    for (DataSnapshot idDs : purReports1.getChildren()) {
                        Integer id = idDs.getValue(Integer.class);
                        waterReport.linkPurityReport(id);
                    }

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
        DatabaseReference dataBaseRef = mDatabase.child("waterReports");
        DatabaseReference ref = dataBaseRef.child("" + waterReport.getReportNumber());
        ref.setValue(waterReport);
    }

    @Override
    public void getNextWaterReportId(Consumer<Integer> callback) {
        DatabaseReference dbRef = mDatabase.child("waterReportId");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference dRef = mDatabase.child("waterReportId");
        dRef.setValue(reportNumber);
    }

    @Override
    public void getAllPurityReports(Consumer<List<PurityReport>> callback) {
        DatabaseReference dRef = mDatabase.child("purityReports");
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PurityReport> purityReports = new ArrayList<>();

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
        DatabaseReference dRef1 = mDatabase.child("purityReports");
        DatabaseReference dRef2 = dRef1.child("" + reportNumber);
        dRef2.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference dRef = mDatabase.child("purityReports");
        DatabaseReference dRef2 = dRef.child("" + purityReport.getReportNumber());
        dRef2.setValue(purityReport);
    }

    @Override
    public void getNextPurityReportId(Consumer<Integer> callback) {
        DatabaseReference dRef = mDatabase.child("purityReportId");
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference dRef = mDatabase.child("purityReportId");
        dRef.setValue(reportNumber);
    }
}
