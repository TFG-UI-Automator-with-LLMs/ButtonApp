package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

public class CheckBoxAction extends Action {

    public CheckBoxAction(UiObject target) {
        super(target, ActionType.CHECKBOX);
    }
    @Override
    public void perform() throws UiObjectNotFoundException {
        this.target.click();
    }
}
