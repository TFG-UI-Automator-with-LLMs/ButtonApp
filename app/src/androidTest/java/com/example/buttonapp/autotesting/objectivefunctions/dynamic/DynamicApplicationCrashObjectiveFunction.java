package com.example.buttonapp.autotesting.objectivefunctions.dynamic;


import androidx.test.uiautomator.UiDevice;

import com.example.buttonapp.autotesting.inagraph.actions.Action;

public class DynamicApplicationCrashObjectiveFunction implements DynamicObjectiveFunction {

    @Override
    public double evaluate(Action action, String appPackage) {
        double result=0;
        try {
            UiDevice device = UiDevice.getInstance();
            action.perform();
            if(!appPackage.equals(device.getCurrentPackageName())){
                result = 1;
            }
        }catch(Exception e){
        }
        return result;
    }
}
