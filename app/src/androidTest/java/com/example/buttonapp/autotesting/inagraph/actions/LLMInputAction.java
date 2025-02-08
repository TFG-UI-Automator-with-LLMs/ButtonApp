package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;

public class LLMInputAction extends InputAction {
    public LLMInputAction(UiObject target, LLMInputGenerator generator) {
        super(target, generator, ActionType.TEXT);
    }
}
