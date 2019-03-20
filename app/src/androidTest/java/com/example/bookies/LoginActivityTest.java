package com.example.bookies;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

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

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> rule  = new  ActivityTestRule<>(LoginActivity.class);

    private LoginActivity activity;



    //TODO: setup mock login environment
    @Before
    public void setUp() throws Exception {

        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    public void invalidUserNameTest(){/*TODO: write*/


    }
    public void invalidPasswordTest(){/*Todo: write*/}
    @Test
    public void validUsernameAndPasswordTest(){
        onView(withId(R.id.username)).perform(typeText("bookiessp2019@gmail.com\n"));
        onView(withId(R.id.password)).perform(typeText("123\n"));
        onView(withId(R.id.loginBtn)).perform(click());

    }



}