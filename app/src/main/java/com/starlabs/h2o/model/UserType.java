package com.starlabs.h2o.model;

import java.io.Serializable;

/**
 * The type of user
 *
 * @author tejun
 */

public enum UserType implements Serializable {
    USER, WORKER, MANAGER, ADMINISTRATOR
}
