package com.example.buttonapp.autotesting.inagraph.actions;


import androidx.test.uiautomator.UiObject;

public class TextInputAction extends InputAction {
    public TextInputAction(UiObject target, TextInputGenerator generator) {
        super(target, generator, ActionType.TEXT);
    }

}
