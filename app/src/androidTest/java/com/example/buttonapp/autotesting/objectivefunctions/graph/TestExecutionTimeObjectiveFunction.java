package com.example.buttonapp.autotesting.objectivefunctions.graph;

import com.example.buttonapp.autotesting.TestCase;

public class TestExecutionTimeObjectiveFunction implements ObjectiveFunction {

    @Override
    public double evaluate(TestCase testcase, String appPackage) {
        long duration=-1;
        try{
            testcase.executeBefore();
            long start=System.currentTimeMillis();
            testcase.executeTest();
            duration=System.currentTimeMillis()-start;
            testcase.executeAfter();
        }catch(Exception e){

        }
        return (double)duration;
    }
}
