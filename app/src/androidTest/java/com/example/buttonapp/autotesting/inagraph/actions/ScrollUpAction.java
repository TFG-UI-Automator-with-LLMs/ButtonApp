package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

public class ScrollUpAction extends Action {

    public ScrollUpAction(UiObject target) {
        super(target, ActionType.SCROLL_UP);
    }

    @Override
    public void perform() throws UiObjectNotFoundException {

        UiScrollable appViews = new UiScrollable (target.getSelector());

        if (value.startsWith("Elements=")) {
            appViews.scrollToBeginning(Integer.valueOf(value.replace("Elements=","")));
        } else if(value.startsWith("toElementById=")) {
            appViews.scrollIntoView(new UiSelector().resourceId(value.replace("toElementById=","")));
        } else if(value.startsWith("toElementByText=")) {
            appViews.scrollIntoView(new UiSelector().text(value.replace("toElementByText=","")));
        } else if(value.startsWith("toElementByDescription=")) {
            appViews.scrollIntoView(new UiSelector().description(value.replace("toElementByDescription=","")));
        } else
            appViews.scrollToBeginning(Integer.MAX_VALUE);
    }
}