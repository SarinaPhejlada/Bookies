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

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> rule  = new  ActivityTestRule<>(LoginActivity.class);

    private LoginActivity activity;
    private EditText username;
    private EditText password;

    Instrumentation.ActivityMonitor monitor = getInstrumentation()
            .addMonitor(HomeActivity.class.getName(),null,false);

    //TODO: setup mock login environment
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
    public void missingUserNameTest(){
        onView(withId(R.id.username)).perform(typeText("\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        assertEquals(username.getError(), "Email is required");
    }
    @Test
    public void invalidUserNameTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail\n"));
        onView(withId(R.id.password)).perform(typeText("123456\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        assertEquals(username.getError(), "Please enter a valid email");
    }
    @Test
    public void missingPasswordTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        assertEquals(password.getError(), "Password is required");
    }
    @Test
    public void invalidPasswordTest(){ //failed
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("123\n"));
        onView(withId(R.id.loginBtn)).perform(click());
    }
    @Test
    public void validUsernameAndPasswordTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("123456\n"));
        onView(withId(R.id.loginBtn)).perform(click());
        Activity homeActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(homeActivity);
        homeActivity.finish();
    }



}