package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionFactory {

    public static Map<UiObject, Action> createInputActions(UiDevice device, Integer seed) {
        TextInputGenerator generator = new TextInputGenerator(seed);
        List<UiObject> inputTexts = ElementIdentifier.findElements(device, "android.widget.EditText");
        Map<UiObject, Action> result = new HashMap<>();
        for (UiObject input : inputTexts) {
            result.put(input, new TextInputAction(input, generator));
        }
        return result;
    }

    public static Map<UiObject, Action> createButtonActions(UiDevice device) {
        Map<UiObject, Action> result = new HashMap<>();
        List<UiObject> buttons = ElementIdentifier.findElements(device, "android.widget.Button");
        for (UiObject input : buttons) {
            result.put(input, new ButtonAction(input));
        }
        return result;
    }

    public static Map<UiObject, Action> createRadioActions(UiDevice device, Integer seed) {
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

    public static Map<UiObject, Action> createActions(UiDevice device, Integer seed) {
        Map<UiObject, Action> actions = new HashMap<>();
        actions.putAll(ActionFactory.createButtonActions(device));
        actions.putAll(ActionFactory.createInputActions(device, seed));
        actions.putAll(ActionFactory.createCheckBoxActions(device));
        actions.putAll(ActionFactory.createRadioActions(device, seed));
        return actions;
    }
}
