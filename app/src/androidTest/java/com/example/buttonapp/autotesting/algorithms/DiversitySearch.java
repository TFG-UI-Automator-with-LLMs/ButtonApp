package com.example.buttonapp.autotesting.algorithms;

import static android.os.SystemClock.sleep;

import android.util.Log;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.diversityActionSelection.ActionSelection;
import com.example.buttonapp.autotesting.inagraph.StartAppAction;
import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.inagraph.actions.ActionFactory;
import com.example.buttonapp.autotesting.util.WriterUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.buttonapp.autotesting.inagraph.CloseAppAction;
import com.example.buttonapp.autotesting.util.Tuple;

public class DiversitySearch {
    long iterations;
    long actionsLength;
    long diversityLength;
    List<Action> beforeActions;
    List<Action> afterActions;
    Map<TestCase, Long> testCases;
    Boolean saveAllTestCases;
    double diferentActions;
    ActionSelection actionSelection;
    private  Random random;

    public DiversitySearch(ActionSelection actionSelection, long iterations, long diversityLength, int actionsLength, String appPackage, Boolean saveAllTestCases) {
        this.iterations = iterations;
        this.actionsLength = actionsLength;
        this.diversityLength = diversityLength;
        this.saveAllTestCases = saveAllTestCases;
        this.diferentActions = 0;
        this.actionSelection = actionSelection;
        beforeActions = new ArrayList<>();
        beforeActions.add(new StartAppAction(appPackage));
        afterActions = new ArrayList<>();
        afterActions.add(new CloseAppAction(appPackage));
        testCases = new HashMap<>();
    }

    public List<TestCase> run(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        actionSelection.setRandom(getRandom());
        int i = 0;
        Tuple<TestCase, Long> testCaseSeedPair;
        while (i < iterations) {
            Log.d("TFG", "Running iteration " + (i + 1));
            testCaseSeedPair = buildTestCase(device, appPackage);
            if (i < diversityLength) {
                testCases.put(testCaseSeedPair.getKey(), testCaseSeedPair.getValue());
            } else {
                testCases = diversityMeasure(testCaseSeedPair);
            }
            diferentActions = evaluateDiversityTestCases(new ArrayList<>(testCases.keySet()));
            Log.d("TFG", "Diferent actions iteration " + (i+1) +": " + diferentActions);
            i++;
        }
        if(!saveAllTestCases){
            WriterUtil writerUtil;
            for (Map.Entry<TestCase, Long> entry: testCases.entrySet()){
                writerUtil = new WriterUtil();
                writerUtil.write(appPackage);
                writerUtil.write(entry.getValue().toString());
                for(Action a: entry.getKey().getTestActions()){
                    writerUtil.write(a.toString());
                }
                sleep(1000);
            }
        }
        return new ArrayList<>(testCases.keySet());
    }

    private Tuple<TestCase, Long> buildTestCase(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        List<Action> testCaseActions;
        List<Action> availableActions;
        Random chosenSeed = new Random();
        Long seed = chosenSeed.nextLong();
        Random seeds = new Random(seed);
        Action chosenAction;
        startApp(appPackage);
        testCaseActions = new ArrayList<>();
        availableActions = createAction(device, seeds.nextInt());
        WriterUtil writerUtil = null;
        if(saveAllTestCases){
            writerUtil = new WriterUtil();
            writerUtil.write(appPackage);
            writerUtil.write(seed.toString());
        }
        while (testCaseActions.size() < actionsLength && availableActions.size() > 0) {
            chosenAction = actionSelection.selectAction(new ArrayList<TestCase>(testCases.keySet()), testCaseActions, availableActions);
            testCaseActions.add(chosenAction);
            Log.d("TFG", "Executing action: " + chosenAction);
            chosenAction.perform();
            if(saveAllTestCases){
                writerUtil.write(chosenAction.toString());
            }
            availableActions = createAction(device, seeds.nextInt());
        }
        closeApp(appPackage);
        return new Tuple<>(new TestCase(appPackage, Collections.EMPTY_SET, beforeActions, testCaseActions, afterActions), seed);
    }

    private List<Action> createAction(UiDevice device, Integer seed) {
        Map<UiObject, Action> actions;
        actions = ActionFactory.createActions(device, seed);
        return new ArrayList<>(actions.values());
    }

    private void closeApp(String appPackage) {
        CloseAppAction action = new CloseAppAction(appPackage);
        action.perform();
    }

    private void startApp(String appPackage) throws UiObjectNotFoundException {
        StartAppAction action = new StartAppAction(appPackage);
        action.perform();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    private Map<TestCase, Long> diversityMeasure(Tuple<TestCase, Long> chosenTestCase) {
        double currentEval = 0;
        double bestEval = diferentActions;
        Map<TestCase, Long> currentTestCases = new HashMap<>(testCases);
        Map<TestCase, Long> bestTestCases = new HashMap<>(testCases);
        for (Map.Entry<TestCase, Long> entry: currentTestCases.entrySet()) {
            currentTestCases = new HashMap<>(testCases);
            currentTestCases.remove(entry.getKey());
            currentTestCases.put(chosenTestCase.getKey(), chosenTestCase.getValue());
            currentEval = evaluateDiversityTestCases(new ArrayList<>(currentTestCases.keySet()));
            if(currentEval > bestEval){
                bestEval = currentEval;
                bestTestCases = new HashMap<>(currentTestCases);
            }
        }
        return bestTestCases;
    }

    private double evaluateDiversityTestCases(List<TestCase> testCases) {
        List<String> actions = new ArrayList<>();
        for (TestCase selectedTestCase : testCases) {
            for (Action action : selectedTestCase.getTestActions()) {
                if (!actions.contains(action.toString())) {
                    actions.add(action.toString());
                }
            }
        }
        return actions.size() * 1.0;
    }

    public Random getRandom() {
        if(random == null){
            random = new Random();
        }
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
