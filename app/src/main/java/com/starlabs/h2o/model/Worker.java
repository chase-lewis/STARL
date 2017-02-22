package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chase on 2/22/17.
 */

public class Worker extends User {

    public Worker(String username, String password, UserType userType) {
        super(username, password, userType);
    }

    public void createReport() {
        System.out.println("Report created!");
    }


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
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with user contents
     */
    private Worker(Parcel in) {
        setName(in.readString());
        setUsername(in.readString());
        setPassword(in.readString());
        setAddress(in.readString());
        setEmail(in.readString());
        setUserType(UserType.valueOf(in.readString()));
    }

    //Required methods for Parcelable Interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getUsername());
        dest.writeString(getPassword());
        dest.writeString(getAddress());
        dest.writeString(getEmail());
        dest.writeString(getUserType().toString());
    }
}
