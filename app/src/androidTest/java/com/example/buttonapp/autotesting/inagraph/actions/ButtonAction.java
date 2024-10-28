package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

public class ButtonAction extends Action {
    public ButtonAction(UiObject button) {
        super(button, ActionType.BUTTON);
    }

    public void perform() throws UiObjectNotFoundException {
        this.target.click();
    }
}
