package com.example.buttonapp.autotesting.objectivefunctions.dynamic;

import com.example.buttonapp.autotesting.inagraph.actions.Action;

public interface DynamicObjectiveFunction {
    public double evaluate(Action action, String appPackage);
}
