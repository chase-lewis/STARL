package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tejun on 2/19/2017.
 */

public class User implements Parcelable {
    private String username;
    private String password;
    private String name;

    //User Profile Stuff
    private String address;
    private String email;

    // TODO add more user profile stuff here

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
        // Default constructor required for calls to firebase
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    //Allows User to be parceable

    private User(Parcel in) {
        name = in.readString();
        username = in.readString();;
        password = in.readString();;

        //User Profile Stuff
        address = in.readString();;
        email = in.readString();;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(address);
        dest.writeString(email);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
