package com.example.bookies;

import android.app.Activity;
import android.app.Instrumentation;
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

public class ForgotActivityTest {

    @Rule
    public ActivityTestRule<ForgotActivity> rule  = new  ActivityTestRule<>(ForgotActivity.class);

    private ForgotActivity activity;
    private EditText email;
    private boolean valid;

    Instrumentation.ActivityMonitor monitor = getInstrumentation()
            .addMonitor(LoginActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
        email = activity.email;
        valid = activity.valid;
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void emptyEmailTest(){
        onView(withId(R.id.email)).perform(typeText("\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        assertEquals("Email is required", email.getError());
    }

    @Test
    public void invalidEmailTest(){
        onView(withId(R.id.email)).perform(typeText("bookiessp2019@gmail\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        assertEquals("Please enter a valid email", email.getError());
    }

    @Test
    public void nonexistingAccountTest() throws Exception{
        onView(withId(R.id.email)).perform(typeText("pphejlada1@student.gsu.edu\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        Thread.sleep(5000);
        assertEquals("Account does not exist for email", email.getError());
    }

    @Test
    public void validEmailTest(){
        onView(withId(R.id.email)).perform(typeText("sarina1116@gmail.com\n"));
        onView(withId(R.id.submitBtn)).perform(click());
        Activity loginActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(loginActivity);
        loginActivity.finish();
    }
}