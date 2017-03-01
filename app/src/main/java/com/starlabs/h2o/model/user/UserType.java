package com.starlabs.h2o.model.user;

import java.io.Serializable;

/**
 * The type of user
 *
 * @author tejun
 */

public enum UserType implements Serializable {
    USER, WORKER, MANAGER, ADMINISTRATOR
}
