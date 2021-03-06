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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> rule  = new  ActivityTestRule<>(LoginActivity.class);

    private LoginActivity activity;
    private EditText username;
    private EditText password;

    Instrumentation.ActivityMonitor monitor = getInstrumentation()
            .addMonitor(HomeActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
        username = activity.username;
        password = activity.password;
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void missingEmailTest(){
        onView(withId(R.id.username)).perform(typeText("\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        assertEquals("Email is required", username.getError());
    }
    @Test
    public void invalidEmailTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail\n"));
        onView(withId(R.id.password)).perform(typeText("123456\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        assertEquals("Please enter a valid email", username.getError());
    }
    @Test
    public void missingPasswordTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        assertEquals("Password is required", password.getError());
    }
    @Test
    public void invalidPasswordTest() throws Exception{
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("123\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        Thread.sleep(5000);
        assertEquals("Incorrect password or account does not exist", password.getError());
    }
    @Test
    public void validEmailAndPasswordTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("123456\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        Activity homeActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(homeActivity);
        homeActivity.finish();
    }



}