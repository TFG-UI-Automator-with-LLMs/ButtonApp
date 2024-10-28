package com.example.buttonapp.autotesting.objectivefunctions.graph;

import com.example.buttonapp.autotesting.TestCase;

public interface ObjectiveFunction {
    public double evaluate(TestCase testcase, String appPackage);
}
