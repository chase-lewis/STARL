package com.starlabs.h2o.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chase on 2/22/17.
 */

public abstract class Person implements Parcelable {
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;
    private UserType userType;

    /**
     * Two parameter constructor for Person object
     *
     * @param username username of Person
     * @param password password of Person.
     */
    public Person(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.name = "";
        this.address = "";
        this.email = "";
        this.userType = userType;
    }


    /**
     * Default constructor for firebase
     */
    public Person() {
        // LEAVE THIS EMPTY FIREBASE NEEDS IT
    }

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with user contents
     */
    private Person(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        address = in.readString();
        email = in.readString();
        userType = UserType.valueOf(in.readString());
    }

    /**
     * Getter for Person Type
     *
     * @return the UserType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Setter for Person Type
     *
     * @param userType the Person Type of Person
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Getter for Username
     *
     * @return username of Person
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for Username
     *
     * @param username username of Person
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Getter for Password
     *
     * @return password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for Password
     *
     * @param password password of Person
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for full name of Person
     *
     * @return full name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for full name of user
     *
     * @param name full name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for address of Person
     *
     * @return address of user
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter for address of Person
     *
     * @param address address of user
     */
    public void setAddress(String address) {
        this.address = address;
    }


    //Allows Person to be parceable

    /**
     * Getter for email of user
     *
     * @return email of user
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
        dest.writeString(userType.toString());
    }
}
