package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chase on 2/22/17.
 */

public class Worker extends User {
    public static final Parcelable.Creator<Worker> CREATOR
            = new Parcelable.Creator<Worker>() {
        public Worker createFromParcel(Parcel in) {
            return new Worker(in);
        }

        public Worker[] newArray(int size) {
            return new Worker[size];
        }
    };

    /**
     * Two parameter constructor for Worker object
     *
     * @param username username of Worker
     * @param password password of Worker
     * @param userType the type of Worker
     */
    public Worker(String username, String password, UserType userType) {
        super(username, password, userType);
    }


    /**
     * Default constructor for firebase
     */
    public Worker() {
        //required for firebase;
    }

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with Worker contents
     */
    private Worker(Parcel in) {
        setName(in.readString());
        setUsername(in.readString());
        setPassword(in.readString());
        setAddress(in.readString());
        setEmail(in.readString());
        setUserType(UserType.valueOf(in.readString()));
    }
}
