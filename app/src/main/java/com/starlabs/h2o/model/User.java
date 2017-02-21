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

    /**
     * Two parameter constructor for User object
     *
     * @param username username of User
     * @param password password of User.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
        // Default constructor required for calls to firebase
    }

    /**
     * Getter for Username
     *
     * @return username username of User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for Username
     *
     * @param  username username of User
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Getter for Password
     *
     * @return password  password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for Password
     *
     * @param password passweord of User
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Getter for full name of User
     *
     * @return name full name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for full name of user
     *
     * @param name  full name of user
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Getter for address of User
     *
     * @return address of user
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter for address of User
     *
     * @param address  address of user
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for email of user
     *
     * @return email of user
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email of user
     *
     * @param email email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }



    //Allows User to be parceable

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in    parcel with user contents
     */
    private User(Parcel in) {
        name = in.readString();
        username = in.readString();;
        password = in.readString();;

        //User Profile Stuff
        address = in.readString();;
        email = in.readString();;
    }

    //Required methods for Parcelable Interface
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
