package com.example.buttonapp.autotesting.tests;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

import android.util.Log;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.algorithms.RandomSearch;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ApplicationCrashObjectiveFunction;

import org.junit.Test;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.INAGraph;
import com.example.buttonapp.autotesting.inagraph.INAGraphBuilder;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ObjectiveFunction;

public class ButtonAppTest {
    @Test
    public void testSimpleApp() throws UiObjectNotFoundException{
        //PARAM: app id
        String appPackageName="com.example.buttonapp";
        //PARAM: goal function
        ObjectiveFunction goalFunction=new ApplicationCrashObjectiveFunction();
        //PARAM: iterations number
        Integer numIterations=10;
        //PARAM: action number per test case
        Integer actionsLength=4;
        //PARAM: device
        UiDevice device=UiDevice.getInstance(getInstrumentation());
        //PARAM: app graph
        INAGraph graph= INAGraphBuilder.getInstance().build(device,appPackageName);
        //
        RandomSearch algorithm=new RandomSearch(goalFunction,numIterations,actionsLength);
        TestCase testCase=algorithm.run(graph,appPackageName);
        Log.d("TFG","Test case found: "+testCase);
        Log.d("TFG","Runnig it...");
        testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();
        Log.d("TFG","Done!");
    }
}
