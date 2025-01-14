package com.example.buttonapp.autotesting.inagraph.actions;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.test.uiautomator.StaleObjectException;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;

public class ActionFactory {

    public static Map<UiObject, Action> createInputActions(UiDevice device, Long seed) {
        String value = null;
        TextInputGenerator generator = new TextInputGenerator(seed, value);
        List<UiObject> inputTexts = ElementIdentifier.findElements(device, "android.widget.EditText");
        Map<UiObject, Action> result = new HashMap<>();
        for (UiObject input : inputTexts) {
            result.put(input, new TextInputAction(input, generator));
        }
        return result;
    }

    public static Map<UiObject, Action> createButtonActions(UiDevice device) {
        Map<UiObject, Action> result = new HashMap<>();
        try{
            List<UiObject> buttons = ElementIdentifier.findElements(device, "android.widget.Button");
            for (UiObject input : buttons) {
                result.put(input, new ButtonAction(input));
            }
        }catch(StaleObjectException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static Map<UiObject, Action> createRadioActions(UiDevice device, Long seed) {
        RadioButtonInputGenerator generator = new RadioButtonInputGenerator(seed);
        Map<UiObject, Action> result = new HashMap<>();
        List<UiObject> buttons = ElementIdentifier.findElements(device, "android.widget.RadioGroup");
        for (UiObject input : buttons) {
            result.put(input, new RadioButtonAction(input, generator));
        }
        return result;
    }

    public static Map<UiObject, Action> createCheckBoxActions(UiDevice device) {
        Map<UiObject, Action> result = new HashMap<>();
        List<UiObject> buttons = ElementIdentifier.findElements(device, "android.widget.CheckBox");
        for (UiObject input : buttons) {
            result.put(input, new CheckBoxAction(input));
        }
        return result;

    }

    public static Map<UiObject, Action> createActions(UiDevice device, Long seed) {
        Map<UiObject, Action> actions = new HashMap<>();
        actions.putAll(ActionFactory.createButtonActions(device));
        actions.putAll(ActionFactory.createInputActions(device, seed));
        actions.putAll(ActionFactory.createCheckBoxActions(device));
        actions.putAll(ActionFactory.createRadioActions(device, seed));
        return actions;
    }
}
