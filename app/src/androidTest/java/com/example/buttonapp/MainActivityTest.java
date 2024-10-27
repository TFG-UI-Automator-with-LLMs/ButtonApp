package com.example.buttonapp;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // Declaramos el activity para la prueba
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Limpiar las preferencias compartidas antes de cada prueba
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Test
    public void testSaveAndChangeText() {
        String textToInput = "Texto de prueba";
        onView(withId(R.id.editText)).perform(replaceText(textToInput));

        onView(withId(R.id.buttonSaveText)).perform(click());

        onView(withId(R.id.editText)).check(matches(withText(textToInput)));

        onView(withId(R.id.buttonChangeText)).perform(click());

        onView(withId(R.id.editText)).check(matches(withText("Texto cambiado!")));

        onView(withId(R.id.buttonSaveText)).perform(click());

        onView(withId(R.id.editText)).check(matches(withText("Texto cambiado!")));
    }
}
