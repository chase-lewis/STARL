package com.starlabs.h2o.dao;

import com.starlabs.h2o.model.user.User;

import java.util.List;
import java.util.function.Consumer;

/**
 * Any class that implements this interface must provide the requested data.
 *
 * @author tejun
 */

public interface ContentProvider {
    void getAllUsers(Consumer<List<User>> callback);
    void getSingleUser(Consumer<User> callback, String username);
    void setUser(User user);
}
