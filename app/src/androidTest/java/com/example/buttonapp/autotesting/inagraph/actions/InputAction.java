package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;


public abstract class InputAction extends Action {
    InputGenerator inputGenerator;
    public InputAction(UiObject target, InputGenerator generator, ActionType actionType){
        super(target, actionType);
        this.inputGenerator=generator;
    }

    public void perform() throws UiObjectNotFoundException {
        inputGenerator.generateInput(target);
    }

}