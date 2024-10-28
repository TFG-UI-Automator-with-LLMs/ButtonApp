package com.example.buttonapp.autotesting.inagraph.actions;


import androidx.test.uiautomator.UiObject;

public class RadioButtonAction extends InputAction {
    public RadioButtonAction(UiObject target, RadioButtonInputGenerator generator) {
        super(target, generator, ActionType.RADIO_BUTTON);
    }
}
