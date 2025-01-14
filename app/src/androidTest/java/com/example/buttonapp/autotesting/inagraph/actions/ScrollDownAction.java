package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

public class ScrollDownAction extends Action{

    public ScrollDownAction(UiObject target) {
        super(target, ActionType.SCROLL_DOWN);
    }

    @Override
    public void perform() throws UiObjectNotFoundException {

        UiScrollable appViews =  new UiScrollable(target.getSelector());

        if (value.startsWith("Elements=")) {
            appViews.scrollToEnd(Integer.valueOf(value.replace("Elements=","")));
        } else if (value.startsWith("toElementById=")) {
            appViews.scrollIntoView(new UiSelector().resourceId(value.replace("toElementById=","")));
        } else if (value.startsWith("toElementByText=")) {
            appViews.scrollIntoView(new UiSelector().text(value.replace("toElementByText=","")));
        } else if (value.startsWith("toElementByDescription=")) {
            appViews.scrollIntoView(new UiSelector().description(value.replace("toElementByDescription=","")));
        } else
            appViews.scrollToEnd(Integer.MAX_VALUE);
    }
}