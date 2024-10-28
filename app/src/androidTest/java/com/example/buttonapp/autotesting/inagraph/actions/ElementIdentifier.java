package com.example.buttonapp.autotesting.inagraph.actions;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiSelector;

import java.util.ArrayList;
import java.util.List;

public class ElementIdentifier {

    public static List<UiObject> findElements(UiDevice device, String finder) {
        List<UiObject> result = new ArrayList<>();
        BySelector sel = resolveSelector(finder);
        List<UiObject2> elements = device.findObjects(sel);
        UiSelector selector = null;
        UiObject button = null;
        for (UiObject2 btn : elements) {
            selector = new UiSelector().resourceId(btn.getResourceName());
            button = device.findObject(selector);
            result.add(button);
        }
        return result;
    }

    private static BySelector resolveSelector(String finder) {
        BySelector result = null;
        finder = finder.substring(finder.lastIndexOf(".") + 1);
        if (finder.equalsIgnoreCase("button")) {
            result = By.clazz(Button.class);
        } else if (finder.equalsIgnoreCase("EditText")) {
            result = By.clazz(EditText.class);
        } else if (finder.equalsIgnoreCase("RadioGroup")) {
            result = By.clazz(RadioGroup.class);
        } else if (finder.equalsIgnoreCase("CheckBox")) {
            result = By.clazz(CheckBox.class);
        } else if (finder.equalsIgnoreCase("Spinner")) {
            result = By.clazz(Spinner.class);
        } else if (finder.equalsIgnoreCase("DatePicker")) {
            result = By.clazz(DatePicker.class);
        }
        return result;
    }
}
