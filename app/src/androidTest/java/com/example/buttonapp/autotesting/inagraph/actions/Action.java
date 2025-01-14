package com.example.buttonapp.autotesting.inagraph.actions;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

public abstract class  Action {

    protected UiObject target;
    protected ActionType actionType;
    protected String value;

    public Action(UiObject target, ActionType actionType){
        this.target=target;
        this.actionType = actionType;
        value = null;
    }

    public abstract void perform() throws UiObjectNotFoundException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;

        boolean result=false;
        //try {
        //    result = target.getClassName().equals(action.target.getClassName()) && target.getText().equals(action.target.getText());
        //}catch(UiObjectNotFoundException e){
        //  e.printStackTrace();
        //}
        result = this.toString().equals(o.toString());
        return result;
    }

    @Override
    public int hashCode() {
        return 31 * this.toString().hashCode();
    }


    public UiObject getTarget() {
        return target;
    }

    public String toString(){
        return actionType+", "+ target.getSelector().toString();
    }

    public enum ActionType{
        BUTTON, TEXT, CHECKBOX, RADIO_BUTTON, START, STOP, GO_BACK, SCROLL_DOWN, SCROLL_UP, COUNT_DOWN
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
