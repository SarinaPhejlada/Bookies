package com.example.bookies;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;

import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class IsbnActivityTest {

    @Rule
    public ActivityTestRule<IsbnActivity> rule  = new  ActivityTestRule<>(IsbnActivity.class);

    private IsbnActivity activity;

    Instrumentation.ActivityMonitor monitor = getInstrumentation()
            .addMonitor(ReviewActivity.class.getName(),null,false);

    Instrumentation.ActivityMonitor monitor2 = getInstrumentation()
            .addMonitor(BarcodeScannerActivity.class.getName(),null,false);


    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void getBookReviewByEnteringValidISBNTest(){

        onView(withId(R.id.isbn)).perform(typeText("0133943038\n"));
        onView(withId(R.id.submitBtn)).perform(click());

        Activity reviewActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(reviewActivity);

        reviewActivity.finish();
    }

    @Test
    public void attemptGettingBookReviewWithInvalidISBNTest(){
        onView(withId(R.id.isbn)).perform(typeText("\n"));
        onView(withId(R.id.submitBtn)).perform(click());

        Activity reviewActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNull(reviewActivity);

        onView(withId(R.id.isbn)).perform(typeText("0133943033\n"));
        onView(withId(R.id.submitBtn)).perform(click());

        reviewActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNull(reviewActivity);

    }

    @Test
    public void scanBarcodeOpensScannerSuccessfullyTest(){
        onView(withId(R.id.takeBtn)).perform(click());
        Activity  barcodeScannerActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2,5000);
        assertNotNull(barcodeScannerActivity);

        barcodeScannerActivity.finish();
    }


}