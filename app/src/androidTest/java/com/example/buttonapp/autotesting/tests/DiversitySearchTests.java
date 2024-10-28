package com.example.buttonapp.autotesting.tests;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

import android.util.Log;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.diversityActionSelection.ActionSelection;

import org.junit.Test;

import java.util.List;

import com.example.buttonapp.autotesting.algorithms.DiversitySearch;
import com.example.buttonapp.autotesting.diversityActionSelection.IntelligentActionSelection;
import com.example.buttonapp.autotesting.diversityActionSelection.RandomActionSelection;

public class DiversitySearchTests {

    //Template for test DiversitySearch algorithm
    private void diversitySearchTestTemplate(String appPackageName, ActionSelection actionSelection,
                                             Integer iterations, Integer diversityLength, Integer actionsLength,
                                             Boolean saveAllTestCases) throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
        DiversitySearch algorithm=new DiversitySearch(actionSelection, iterations, diversityLength,
                actionsLength, appPackageName, saveAllTestCases);
        List<TestCase> testCases=algorithm.run(mDevice, appPackageName);
        Log.d("TFG","Test cases founded: " + testCases.size());
    }

    //This test use DiversitySearch algorithm with RandomActionSelection into ButtomApp 1
    @Test
    public void testButtomApp1RandomActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid";
        ActionSelection actionSelection=new RandomActionSelection();
        Integer iterations = 10;
        Integer diversityLength = 5;
        Integer actionLength = 1;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with IntelligentActionSelection into ButtomApp 1
    @Test
    public void testButtomApp1IntelligentActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid";
        ActionSelection actionSelection=new IntelligentActionSelection();
        Integer iterations = 10;
        Integer diversityLength = 5;
        Integer actionLength = 1;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with RandomActionSelection ButtomApp 2
    @Test
    public void testButtomApp2RandomActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid2";
        ActionSelection actionSelection=new RandomActionSelection();
        Integer iterations = 10;
        Integer diversityLength = 4;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with IntelligentActionSelection into into ButtomApp 2
    @Test
    public void testButtomApp2IntelligentActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "com.example.testingandroid2";
        ActionSelection actionSelection=new IntelligentActionSelection();
        Integer iterations = 10;
        Integer diversityLength = 4;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with IntelligentActionSelection into TextInputApp
    @Test
    public void testTextInputAppRandomActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno.textinputapp";ActionSelection actionSelection=new RandomActionSelection();
        Integer iterations = 3;
        Integer diversityLength = 2;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with IntelligentActionSelection into TextInputApp
    @Test
    public void testTextInputAppIntelligentActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno.textinputapp";ActionSelection actionSelection=new RandomActionSelection();
        Integer iterations = 3;
        Integer diversityLength = 2;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with RandomActionSelection into WidgetApp
    @Test
    public void testWidgetAppRandomActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno";
        ActionSelection actionSelection=new RandomActionSelection();
        Integer iterations = 3;
        Integer diversityLength = 2;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with IntelligentActionSelection into WidgetApp
    @Test
    public void testWidgetAppIntelligentActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.alumno";
        ActionSelection actionSelection=new IntelligentActionSelection();
        Integer iterations = 10;
        Integer diversityLength = 2;
        Integer actionLength = 2;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    ///This test use DiversitySearch algorithm with RandomActionSelection into DiversityApp
    @Test
    public void testDiversityAppRandomActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.diversityapp";ActionSelection actionSelection=new RandomActionSelection();
        Integer iterations = 30;
        Integer diversityLength = 10;
        Integer actionLength = 6;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }

    //This test use DiversitySearch algorithm with IntelligentActionSelection into DiversityApp
    @Test
    public void testDiversityIntelligentActionSelection() throws UiObjectNotFoundException {
        String appPackageName = "esadrcanfer.us.diversityapp";
        ActionSelection actionSelection=new IntelligentActionSelection();
        Integer iterations = 30;
        Integer diversityLength = 10;
        Integer actionLength = 6;
        Boolean saveAllTestCases = false;
        diversitySearchTestTemplate(appPackageName, actionSelection, iterations, diversityLength, actionLength, saveAllTestCases);
    }
}
