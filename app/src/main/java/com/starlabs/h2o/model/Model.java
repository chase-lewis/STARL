package com.starlabs.h2o.model;

/**
 * Created by chase on 2/20/17.
 */

public class Model {
    private static final Model instance = new Model();
    private User user;

    /**
     * Getter for the singleton model
     * @return The instance of the model
     */
    public static Model getInstance() {
        return instance;
    }

    /**
     * Setter for the user tracker
     * @param newUser The user to be set to user
     */
    public void setUser(User newUser) {
        user = newUser;
    }

    /**
     * Getter for the user instance
     * @return The user instance
     */
    public User getUser() {
        return user;
    }
}
