package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

public abstract class InputGenerator {

    public abstract void generateInput(UiObject object) throws UiObjectNotFoundException;
}
