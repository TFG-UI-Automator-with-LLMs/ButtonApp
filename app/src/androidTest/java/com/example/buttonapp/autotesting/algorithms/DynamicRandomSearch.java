package com.example.buttonapp.autotesting.algorithms;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.CloseAppAction;
import com.example.buttonapp.autotesting.inagraph.StartAppAction;
import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.inagraph.actions.ActionFactory;
import com.example.buttonapp.autotesting.objectivefunctions.dynamic.DynamicObjectiveFunction;
import com.example.buttonapp.autotesting.util.WriterUtil;

public class DynamicRandomSearch {
    DynamicObjectiveFunction objective;
    long iterations;
    long actionsLength;
    List<Action> beforeActions;
    List<Action> afterActions;
    List<Action> testActions;
    Boolean saveAllTestCases;
    Random random;
    String prompt;

    public DynamicRandomSearch(DynamicObjectiveFunction objective, long iterations, int actionsLength, String appPackage, Boolean saveAllTestCases, String prompt) {
        this.objective = objective;
        this.iterations = iterations;
        this.actionsLength=actionsLength;
        this.saveAllTestCases = saveAllTestCases;
        this.prompt = prompt;
        beforeActions=new ArrayList<>();
        beforeActions.add(new StartAppAction(appPackage));
        afterActions=new ArrayList<>();
        afterActions.add(new CloseAppAction(appPackage));
        testActions=new ArrayList<>();
    }

    public TestCase run(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        int i=0;
        List<Action> testCaseActions;
        List<Action> availableActions;
        WriterUtil writerUtil = null;
        Action chosenAction;
        Long selectedSeed=-1000l;
        double currentBestEval = -100;
        double eval;
        while(i<iterations){
            Log.d("TFG","Running iteration "+(i+1));
            Random chosenSeed = new Random();
            Long seed = chosenSeed.nextLong();
            Random seeds = new Random(seed);
            eval = 0;
            if(saveAllTestCases){
                writerUtil = new WriterUtil();
                writerUtil.write(appPackage);
                writerUtil.write(seed.toString());
            }
            startApp(appPackage);
            testCaseActions = new ArrayList<>();
            availableActions = createAction(device, seeds.nextLong());
            while(testCaseActions.size()<actionsLength && availableActions.size() > 0){
                chosenAction=availableActions.get(getRandom().nextInt(availableActions.size()));
                testCaseActions.add(chosenAction);
                Log.d("TFG","Executing action: "+ chosenAction);
                if(saveAllTestCases){
                    writerUtil.write(chosenAction.toString());
                }
                eval += objective.evaluate(chosenAction, appPackage);
                String appName = UiDevice.getInstance().getCurrentPackageName();
                if(!appName.equals(appPackage)){
                    break;
                }
                availableActions = createAction(device, seeds.nextLong());
            }
            Log.d("TFG", "Eval: " + eval);
            if(eval>currentBestEval){
                currentBestEval=eval;
                selectedSeed = seed;
                testActions = new ArrayList<>();
                testActions.addAll(testCaseActions);
            } else if (eval == currentBestEval && (testActions.size() == 0 || testActions.size()> testCaseActions.size())){
                currentBestEval=eval;
                selectedSeed = seed;
                testActions = new ArrayList<>();
                testActions.addAll(testCaseActions);
            }
            closeApp(appPackage);
            i++;
        }


        if(!saveAllTestCases){
            writerUtil = new WriterUtil();
            writerUtil.write(appPackage);
            writerUtil.write(selectedSeed.toString());
            for(Action a: testActions){
                writerUtil.write(a.toString());
            }
        }
        return new TestCase(appPackage, Collections.EMPTY_SET,beforeActions,testActions,afterActions, new ArrayList<>(), new ArrayList<>());
    }

    private List<Action> createAction(UiDevice device, Long seed) {
        Map<UiObject, Action> actions;
        actions = ActionFactory.createActions(device, seed, prompt);
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

    public Random getRandom(){
        if(random == null){
            random = new Random();
        }
        return random;
    }

    public void setRandom(Random random){
        this.random = random;
    }

    /**public boolean isSameNode(UiDevice device, List<Action> availableActions, Random random) {
     boolean result = true;
     List<Action> actions = new ArrayList<>(ActionFactory.createActions(device, random).values());
     for (int i = 0; i < actions.size() && result; i++)
     result = (result && availableActions.contains(actions.get(i)));
     result = (result && availableActions.size() == (actions.size()));
     return result;
     }**/
}
