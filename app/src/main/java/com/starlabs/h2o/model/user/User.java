package com.starlabs.h2o.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.util.Random;

/**
 * User POJO
 *
 * @author teju, chase, rishi, sangjue
 */
public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public static final int MAX_PASS_LENGTH = 15;
    public static final int MIN_PASS_LENGTH = 5;
    public static final int MAX_USER_LENGTH = 20;
    public static final int MIN_USER_LENGTH = 3;
    public static final int MAX_EMAIL_LENGTH = 120;
    public static final int MIN_EMAIL_LENGTH = 4;
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
     * @param userType type of user
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
     * Checks if a username is valid
     *
     * @param username the username to check
     * @return true if valid
     */
    public static boolean isUsernameValid(String username) {
        if ((username == null) || (username.length() > MAX_USER_LENGTH)
                || (username.length() < MIN_USER_LENGTH)) {
            return false;
        }
        boolean containsDigit = false;
        for (int i = 0; i < username.length(); i++) {
            char currentChar = username.charAt(i);
            if (Character.isDigit(currentChar)) {
                containsDigit = true;
            }
            if (Character.isWhitespace(currentChar)) {
                return false;
            }
        }

        return containsDigit;
    }

    /**
     * Checks if a password is valid
     *
     * @param password the password to check
     * @return true if valid
     */
    public static boolean isPasswordValid(String password) {
        if ((password == null) || (password.length() < MIN_PASS_LENGTH)
                || (password.length() > MAX_PASS_LENGTH)) {
            return false;
        }
        boolean containsUpper = false;
        boolean containsLower = false;
        boolean containsDigit = false;
        for (int i = 0; i < password.length(); i++) {
            char charTest = password.charAt(i);
            if (Character.isDigit(charTest)) {
                containsDigit = true;
            } else if (Character.isUpperCase(charTest)) {
                containsUpper = true;
            } else if (Character.isLowerCase(charTest)) {
                containsLower = true;
            } else if (Character.isWhitespace(charTest)) {
                return false;
            }
        }
        return containsDigit && containsLower && containsUpper;
    }

    /**
     * Checks if a email is valid
     *
     * @param email the email to check
     * @return true if valid
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.length() < MIN_EMAIL_LENGTH
                || email.length() > MAX_EMAIL_LENGTH) {
            return false;
        }
        boolean containsUpper = false;
        boolean containsLower = false;
        boolean containsDigit = false;
        boolean containsAt = false;
        boolean containsDot = false;
        for (int i = 0; i < email.length(); i++) {
            char charTest = email.charAt(i);
            if (Character.isDigit(charTest)) {
                containsDigit = true;
            } else if (Character.isUpperCase(charTest)) {
                containsUpper = true;
            } else if (Character.isLowerCase(charTest)) {
                containsLower = true;
            } else if (charTest == '@') {
                containsAt = true;
            } else if (charTest == '.') {
                containsDot = true;
            } else if (Character.isWhitespace(charTest)) {
                return false;
            }
        }
        return (containsDigit || containsLower || containsUpper) && containsAt && containsDot;
    }

    /**
     * Resets the user's password with a new randomly generated one.
     */
    public void resetPassword() {
        // Alphanumeric password generation
        this.password = RandomStringUtils.randomAlphanumeric(12);;
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


    //Allows User to be parcelable

    /**
     * Getter for address of User
     *
     * @return address of user
     */
    public CharSequence getAddress() {
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
