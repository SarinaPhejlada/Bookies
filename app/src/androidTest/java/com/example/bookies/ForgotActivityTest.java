package com.example.bookies;

import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class ForgotActivityTest {

    @Rule
    public ActivityTestRule<ForgotActivity> rule  = new  ActivityTestRule<>(ForgotActivity.class);

    private ForgotActivity activity;
    private EditText email;
    private String error;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
        email = activity.email;
        error = activity.error;
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void emptyEmailTest(){
        onView(withId(R.id.email)).perform(typeText("\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        assertEquals(email.getError(), "Email is required");
    }

    @Test
    public void invalidEmailTest(){
        onView(withId(R.id.email)).perform(typeText("bookiessp2019@gmail\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        assertEquals(email.getError(), "Please enter a valid email");
    }

    @Test
    public void nonexistingAccountTest(){ //failed
        onView(withId(R.id.email)).perform(typeText("pphejlada1@student.gsu.edu\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        assertEquals(error, "Error: account does not exist");
    }

    @Test
    public void validEmailTest(){ //failed
        onView(withId(R.id.email)).perform(typeText("sarina1116@gmail.com\n"));
        onView(withId(R.id.submitBtn)).perform(click());
    }
}