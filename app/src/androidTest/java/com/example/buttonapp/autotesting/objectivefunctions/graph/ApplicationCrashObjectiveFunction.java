package com.example.buttonapp.autotesting.objectivefunctions.graph;

import android.util.Log;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;

public class ApplicationCrashObjectiveFunction implements ObjectiveFunction {
    @Override
    public double evaluate(TestCase test, String appPackage) {
        double result=0;
        UiDevice device = UiDevice.getInstance();
        try {
            test.executeBefore();
            test.executeTest();
        }catch(Exception e){
            Log.d("TFG", "Se ha cerrado la aplicación");
            result=1;
        } finally {
            try {

                if(!appPackage.equals(device.getCurrentPackageName())){
                    result = 1;
                }
                test.executeAfter();
            } catch (UiObjectNotFoundException e1) {

            }
        }
        return result;
    }
}
