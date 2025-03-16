package com.example.buttonapp.autotesting.tests;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

import android.util.Log;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.objectivefunctions.dynamic.DynamicApplicationCrashObjectiveFunction;
import com.example.buttonapp.autotesting.objectivefunctions.dynamic.DynamicTestExecutionTimeObjectiveFunction;

import org.junit.Test;

import com.example.buttonapp.autotesting.algorithms.DynamicRandomSearch;
import com.example.buttonapp.autotesting.objectivefunctions.dynamic.DynamicObjectiveFunction;

public class DynamicRandomSearchTests {

    //Template for test DynamicRandomSearch algorithm
    private void dynamicRandomSearchTestTemplate(String appPackageName, DynamicObjectiveFunction objective,
                                                 Integer iterations, Integer actionsLength,
                                                 Boolean saveAllTestCases) throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
        String prompt = null;
        DynamicRandomSearch algorithm = new DynamicRandomSearch(objective, iterations, actionsLength, appPackageName, saveAllTestCases, prompt);
        TestCase testCase = algorithm.run(mDevice, appPackageName);
        Log.d("TFG", "Test case found: " + testCase);
        Log.d("TFG", "Runnig it...");
        testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();
        Log.d("TFG", "Done!");
    }

    //This test use DynamicRandomSearch algorithm with DynamicApplicationCrashObjetiveFunction into ButtomApp 1
    @Test
    public void testButtomApp1Crash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid";
        DynamicObjectiveFunction objective=new DynamicApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 3;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicMaxExecutionTimeObjetiveFunction into ButtomApp 1
    @Test
    public void testButtomApp1MaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid";
        DynamicObjectiveFunction objective=new DynamicTestExecutionTimeObjectiveFunction(1000);
        Integer iterations = 40;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);

    }

    //This test use DynamicRandomSearch algorithm with DynamicApplicationCrashObjetiveFunction into ButtomApp 2
    @Test
    public void testButtomApp2Crash() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid2";
        DynamicObjectiveFunction objective=new DynamicApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicMaxExecutionTimeObjetiveFunction into ButtomApp 2
    @Test
    public void testButtomApp2MaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid2";
        DynamicObjectiveFunction objective=new DynamicTestExecutionTimeObjectiveFunction(3000);
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicApplicationCrashObjetiveFunction into TextInputApp
    @Test
    public void testTextInputAppCrash() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno.textinputapp";
        DynamicObjectiveFunction objective=new DynamicApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicMaxExecutionTimeObjetiveFunction into TextInputApp
    @Test
    public void testTextInputAppMaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno.textinputapp";
        DynamicObjectiveFunction objective=new DynamicTestExecutionTimeObjectiveFunction(3000);
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicApplicationCrashObjetiveFunction into WidgetApp
    @Test
    public void testWidgetAppCrash() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno";
        DynamicObjectiveFunction objective=new DynamicApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicMaxExecutionTimeObjetiveFunction into WidgetApp
    @Test
    public void testWidgetAppMaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno";
        DynamicObjectiveFunction objective=new DynamicTestExecutionTimeObjectiveFunction(3000);
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicApplicationCrashObjetiveFunction into DiversityApp
    @Test
    public void testDiversityAppCrash() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.diversityapp";
        DynamicObjectiveFunction objective=new DynamicApplicationCrashObjectiveFunction();
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }

    //This test use DynamicRandomSearch algorithm with DynamicMaxExecutionTimeObjetiveFunction into DiversityApp
    @Test
    public void testDiversityMaxExecutionTime() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.diversityapp";
        DynamicObjectiveFunction objective=new DynamicTestExecutionTimeObjectiveFunction(3000);
        Integer iterations = 10;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        dynamicRandomSearchTestTemplate(appPackageName, objective, iterations, actionLength, saveAllTestCases);
    }


}
