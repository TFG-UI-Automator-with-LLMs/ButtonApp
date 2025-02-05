package com.example.buttonapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Si necesitas limpiar SharedPreferences, etc., hazlo aquí
    }

    @Test
    public void testGrantManageAllFilesPermissionAndCheckText() throws UiObjectNotFoundException {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                .setData(Uri.parse("package:" + "com.example.buttonapp"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
                .startActivity(intent);

        // El switch esta en Ajustes
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.waitForIdle(2000);

        // Buscar switchWidget, puede estar en com.android.settings:id/ o android:id/
        UiObject switchWidget = device.findObject(
                new UiSelector().resourceId("com.android.settings:id/switchWidget")
        );
        if (switchWidget.exists() && switchWidget.isEnabled()) {
            switchWidget.click();
            // Hacer click en el allow access
            UiObject allowButton = device.findObject(new UiSelector().textMatches("(?i)allow|permitir"));
            if (allowButton.exists()) {
                allowButton.click();
            }
        }
        // Cerrar la pantalla de Ajustes
        UiObject navUp = device.findObject(new UiSelector().description("Navigate up"));
        if (navUp.exists()) {
            navUp.click();
        } else {
            device.pressBack();
        }
        // Prueba de la app
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
