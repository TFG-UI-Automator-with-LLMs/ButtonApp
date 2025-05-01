package com.example.buttonapp.autotesting.tests;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

import android.util.Log;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.algorithms.RandomSearch;
import com.example.buttonapp.autotesting.inagraph.INAGraph;
import com.example.buttonapp.autotesting.inagraph.INAGraphBuilder;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ApplicationCrashObjectiveFunction;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ObjectiveFunction;
import com.example.buttonapp.autotesting.objectivefunctions.graph.TestExecutionTimeObjectiveFunction;

import org.junit.Test;

public class RandomSearchTests {

    //Template for test RandomSearch algorithm
    private void randomSearchTestTemplate(String appPackageName, ObjectiveFunction objective, Integer iterations, Integer actionsLength, String prompt) throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
        INAGraph graph= INAGraphBuilder.getInstance().build(mDevice,appPackageName,prompt);
        RandomSearch algorithm=new RandomSearch(objective,iterations,actionsLength);
        TestCase testCase=algorithm.run(graph,appPackageName);
        Log.d("TFG","Test case found: "+testCase);
        Log.d("TFG","Runnig it...");
        testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();
        Log.d("TFG","Done!");
    }

    //This test use RandomSearch algorithm with ApplicationCrashObjetiveFunction into ButtomApp 1
    @Test
    public void testButtomApp1Crash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new ApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with MaxExecutionTimeObjetiveFunction into ButtomApp 1
    @Test
    public void testButtomApp1MaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new TestExecutionTimeObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with ApplicationCrashObjetiveFunction into ButtomApp 2
    @Test
    public void testButtomApp2Crash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new ApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with MaxExecutionTimeObjetiveFunction into ButtomApp 2
    @Test
    public void testButtomApp2MaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new TestExecutionTimeObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with ApplicationCrashObjetiveFunction into TextInputApp
    @Test
    public void testTextInputAppCrash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new ApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with MaxExecutionTimeObjetiveFunction into TextInputApp
    @Test
    public void testTextInputAppMaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new TestExecutionTimeObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with ApplicationCrashObjetiveFunction into WidgetApp
    @Test
    public void testWidgetAppCrash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new ApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with MaxExecutionTimeObjetiveFunction into WidgetApp
    @Test
    public void testWidgetAppMaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new TestExecutionTimeObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with ApplicationCrashObjetiveFunction into DiversityApp
    @Test
    public void testDiversityAppCrash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new ApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }

    //This test use RandomSearch algorithm with MaxExecutionTimeObjetiveFunction into DiversityApp
    @Test
    public void testDiversityMaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.buttonapp";
        ObjectiveFunction objective=new TestExecutionTimeObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        randomSearchTestTemplate(appPackageName, objective, iterations, actionLength, "");
    }


}
