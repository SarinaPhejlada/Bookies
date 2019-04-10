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

public class CreateActivityTest {
    @Rule
    public ActivityTestRule<CreateActivity> rule  = new  ActivityTestRule<>(CreateActivity.class);
    private CreateActivity activity;
    private EditText password;
    private EditText email;
    private boolean exist;
    Instrumentation.ActivityMonitor monitor = getInstrumentation()
            .addMonitor(HomeActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
        email = activity.email;
        password = activity.password;
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void missingEmailTest(){
        onView(withId(R.id.email)).perform(typeText("\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));
        onView(withId(R.id.createBtn)).perform(click());
        assertEquals("Email is required", email.getError());
    }

    @Test
    public void invalidEmailTest(){
        onView(withId(R.id.email)).perform(typeText("bookiessp2019@gmail\n"));
        onView(withId(R.id.password)).perform(typeText("password\n"));
        onView(withId(R.id.createBtn)).perform(click());
        assertEquals("Please enter a valid email", email.getError());
    }

    @Test
    public void missingPasswordTest(){
        onView(withId(R.id.email)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));
        onView(withId(R.id.createBtn)).perform(click());
        assertEquals("Password is required", password.getError());
    }

    @Test
    public void invalidPasswordTest(){
        onView(withId(R.id.email)).perform(typeText("helloppl116@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("123\n"));
        onView(withId(R.id.createBtn)).perform(click());
        assertEquals("Password must be at least 6 characters", password.getError());
    }

    @Test
    public void existingAccountTest() throws Exception{
        onView(withId(R.id.email)).perform(typeText("sarina1116@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("sarina\n"));
        onView(withId(R.id.createBtn)).perform(click());
        Thread.sleep(5000);
        assertEquals("Account already registered", email.getError());
    }

    @Test
    public void validEmailAndPasswordTest(){
        onView(withId(R.id.email)).perform(typeText("pphejlada1@student.gsu.edu\n"));
        onView(withId(R.id.password)).perform(typeText("password\n"));
        onView(withId(R.id.createBtn)).perform(click());
        Activity homeActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(homeActivity);
        homeActivity.finish();
    }
}