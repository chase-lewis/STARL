package com.starlabs.h2o.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author chase
 */

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;
    private UserType userType;

    /**
     * Two parameter constructor for User object
     *
     * @param username username of User
     * @param password password of User.
     */
    public User(String username, String password, UserType userType) {
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
    public User() {
        // LEAVE THIS EMPTY FIREBASE NEEDS IT
    }

    /**
     * constructor that takes in a parcel and reads data
     *
     * @param in parcel with user contents
     */
    private User(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        address = in.readString();
        email = in.readString();
        userType = UserType.valueOf(in.readString());
    }

    /**
     * Checks if the given password matches this user's password.
     *
     * @param password the password to check
     * @return true if matches
     */
    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Getter for User Type
     *
     * @return the UserType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Setter for User Type
     *
     * @param userType the User Type of User
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Getter for Username
     *
     * @return username of User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for Username
     *
     * @param username username of User
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
     * @param password password of User
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for full name of User
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
     * @param address address of user
     */
    public void setAddress(String address) {
        this.address = address;
    }


    //Allows User to be parceable

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

    /**
     * Checks if a username is valid
     *
     * @param username the username to check
     * @return true if valid
     */
    public static boolean isUsernameValid(String username) {
        return username.length() >= 4;
    }

    /**
     * Checks if a password is valid
     *
     * @param password the password to check
     * @return true if valid
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }
}
