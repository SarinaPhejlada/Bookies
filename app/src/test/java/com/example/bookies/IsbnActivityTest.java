package com.example.bookies;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsbnActivityTest {

    //@Rule

    private IsbnActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = new IsbnActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void isValidISBNFormatValidFormatTest(){

        assertTrue(activity.isValidISBNFormat("0133943038"));
    }

    @Test
    public void isValidISBNFormatInvalidFormatTest(){

        assertFalse(activity.isValidISBNFormat(""));
        assertFalse(activity.isValidISBNFormat("123"));
        assertFalse(activity.isValidISBNFormat("asdfj"));
        assertFalse(activity.isValidISBNFormat("123adf2389"));

    }
}