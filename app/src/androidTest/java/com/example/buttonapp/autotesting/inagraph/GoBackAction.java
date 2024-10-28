package com.example.buttonapp.autotesting.inagraph;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.inagraph.actions.Action;

public class GoBackAction extends Action {
    UiDevice device;

    GoBackAction(UiDevice device){
        super(null, ActionType.GO_BACK);
        this.device=device;
    }

    @Override
    public void perform() throws UiObjectNotFoundException {
            device.pressBack();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoBackAction)) return false;
        if (!super.equals(o)) return false;

        GoBackAction that = (GoBackAction) o;

        return device != null ? device.equals(that.device) : that.device == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (device != null ? device.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Press Back ";
    }
}
