package com.example.bookies;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewActivityTest {

    @Rule
    public ActivityTestRule<ReviewActivity> rule  = new  ActivityTestRule<>(ReviewActivity.class);

    private ReviewActivity activity;

    Book book;

    @Before
    public void setUp() throws Exception {
        book = new Book();
        book.setISBNNumber("testISBN");
        book.setImageLink("testImageLink");
        book.setSeller("testSeller");
        book.setTitle("testTitle");
        book.setReview("testReview");

        activity = rule.getActivity();

    }

    @After
    public void tearDown() throws Exception {
        activity = null;
        book = null;
    }

   // @Test
    public void displayOfInformationTest(){

    }

}