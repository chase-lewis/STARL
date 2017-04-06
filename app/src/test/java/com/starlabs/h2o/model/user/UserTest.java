package com.starlabs.h2o.model.user;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the User Plain Old Java Object
 *
 * @author Chase, Rishi, Sungjae
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

    @Test
    public void isUsernameValidTest() {
        //null check
        assertFalse(User.isUsernameValid(null));

        //check for white space
        assertFalse(User.isUsernameValid("dark knight"));

        //missing digit check
        assertFalse(User.isUsernameValid("user"));

        //valid check
        assertTrue(User.isUsernameValid("user101"));
        assertTrue(User.isUsernameValid("manager1"));

        //check for boundaries
        assertFalse(User.isUsernameValid("averyfantasticuser123")); //21 chars
        assertTrue(User.isUsernameValid("averyfantasticuser12")); //20 chars
        assertTrue(User.isUsernameValid("ba1")); //3 chars
        assertFalse(User.isUsernameValid("ba")); //2 chars
        assertTrue(User.isUsernameValid("jackets1")); //8 chars
    }

    @Test
    public void isEmailValidTest() {
        //null check
        assertFalse(User.isEmailValid(null));

        //check for white space
        assertFalse(User.isEmailValid("dark knight@gmail.com"));

        //valid check
        assertTrue(User.isEmailValid("user101@gmail.com"));
        assertTrue(User.isEmailValid("manager1@gmail.com"));

        //missing @ check
        assertFalse(User.isEmailValid("avey23gmail.com"));

        //missing dot check
        assertFalse(User.isEmailValid("avey23@gmailcom"));

        //check for boundaries
        assertFalse(User.isEmailValid("averyfantasy3324@gmail.com")); //26 chars
        assertTrue(User.isEmailValid("avey23@gmail.com")); //16 chars

    }
}
