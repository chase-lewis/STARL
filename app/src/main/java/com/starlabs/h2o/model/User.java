package com.starlabs.h2o.model;

/**
 * Created by tejun on 2/19/2017.
 */

public class User {
    private String username;
    private String password;
    private String name;
    // TODO add more user profile stuff here

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {

        this.username = username;
    }

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

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
