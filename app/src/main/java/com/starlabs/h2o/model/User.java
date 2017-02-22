package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tejun on 2/19/2017.
 */

public class User extends Person {
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * Two parameter constructor for User object
     *
     * @param username username of User
     * @param password password of User
     * @param userType the type of User
     */
    public User(String username, String password, UserType userType) {
        super(username, password, userType);
    }


    /**
     * Default constructor for firebase
     */
    public User() {
        // LEAVE THIS EMPTY FIREBASE NEEDS IT
    }

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with User contents
     */
    private User(Parcel in) {
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
