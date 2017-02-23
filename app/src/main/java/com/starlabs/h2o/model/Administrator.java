package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chase on 2/22/17.
 */

public class Administrator extends Person {
    public static final Parcelable.Creator<Administrator> CREATOR
            = new Parcelable.Creator<Administrator>() {
        public Administrator createFromParcel(Parcel in) {
            return new Administrator(in);
        }

        public Administrator[] newArray(int size) {
            return new Administrator[size];
        }
    };

    /**
     * Two parameter constructor for Administrator object
     *
     * @param username username of Administrator
     * @param password password of Administrator
     * @param userType the type of Administrator
     */
    public Administrator(String username, String password, UserType userType) {
        super(username, password, userType);
    }


    /**
     * Default constructor for firebase
     */
    public Administrator() {
        //required for firebase;
    }

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with Administrator contents
     */
    private Administrator(Parcel in) {
        setName(in.readString());
        setUsername(in.readString());
        setPassword(in.readString());
        setAddress(in.readString());
        setEmail(in.readString());
        setUserType(UserType.valueOf(in.readString()));
    }
}
