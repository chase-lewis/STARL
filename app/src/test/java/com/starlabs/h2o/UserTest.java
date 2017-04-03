package com.starlabs.h2o;

import com.starlabs.h2o.model.user.User;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the User Plain Old Java Object
 *
 * @author Chase
 */
public class UserTest {
    @Test
    public void isPasswordValidTest() {
        //null
        assertFalse(User.isPasswordValid(null));

        //missing one attribute
        assertFalse(User.isPasswordValid("aaaaaaa8"));
        assertFalse(User.isPasswordValid("AAAAAAA8"));
        assertFalse(User.isPasswordValid("aaaaaaaA"));

        //missing two attributes
        assertFalse(User.isPasswordValid("aaaaaaaa"));
        assertFalse(User.isPasswordValid("AAAAAAAA"));
        assertFalse(User.isPasswordValid("88888888"));

        //missing all attributes
        assertFalse(User.isPasswordValid("&&&&&&&&"));

        //valid pass
        assertTrue(User.isPasswordValid("validPass11"));

        //includes whitespace
        assertFalse(User.isPasswordValid("not valid11"));

        //boundary tests for length
        assertFalse(User.isPasswordValid("aAA4"));
        assertTrue(User.isPasswordValid("aAAA5"));
        assertTrue(User.isPasswordValid("aAAAAAAA10"));
        assertTrue(User.isPasswordValid("aAAAAAAAAAAAA15"));
        assertFalse(User.isPasswordValid("aAAAAAAAAAAAAA16"));
    }
}
