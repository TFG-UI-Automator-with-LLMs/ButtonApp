package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

public class LLMInputAction extends InputAction {

    public LLMInputAction(UiObject target, LLMInputGenerator generator) {
        super(target, generator, ActionType.LLMTEXTINPUT);
    }

}
