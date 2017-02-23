package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chase on 2/22/17.
 */

public class Manager extends Worker {
    public static final Parcelable.Creator<Manager> CREATOR
            = new Parcelable.Creator<Manager>() {
        public Manager createFromParcel(Parcel in) {
            return new Manager(in);
        }

        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

    /**
     * Two parameter constructor for Manager object
     *
     * @param username username of Manager
     * @param password password of Manager
     * @param userType the type of Manager
     */
    public Manager(String username, String password, UserType userType) {
        super(username, password, userType);
    }


    /**
     * Default constructor for firebase
     */
    public Manager() {
        //required for firebase;
    }

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with Manager contents
     */
    private Manager(Parcel in) {
        setName(in.readString());
        setUsername(in.readString());
        setPassword(in.readString());
        setAddress(in.readString());
        setEmail(in.readString());
        setUserType(UserType.valueOf(in.readString()));
    }
}
