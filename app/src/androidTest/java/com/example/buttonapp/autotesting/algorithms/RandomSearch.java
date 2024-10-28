package com.example.buttonapp.autotesting.algorithms;

import android.util.Log;

import androidx.test.uiautomator.UiObjectNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.CloseAppAction;
import com.example.buttonapp.autotesting.inagraph.INAGraph;
import com.example.buttonapp.autotesting.inagraph.StartAppAction;
import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ObjectiveFunction;

public class RandomSearch {

    ObjectiveFunction objective;
    long iterations;
    long actionsLength;
    Random random;

    public RandomSearch(ObjectiveFunction objective, long iterations, int actionsLength) {
        this.objective = objective;
        this.iterations = iterations;
        this.actionsLength=actionsLength;
    }

    public TestCase run(INAGraph graph, String appPackage) throws UiObjectNotFoundException {
        Log.d("TFG","Running iteration 1");
        TestCase candidate=buildRandomTestCase(graph,appPackage);
        Log.d("TFG", "Choosen actions: " + candidate);
        TestCase result=candidate;
        double eval=objective.evaluate(result, appPackage);
        Log.d("TFG", "Eval: " + eval);
        double currentBestEval=eval;
        int i=1;
        while(i<iterations){
            Log.d("TFG","Running iteration "+(i+1));
            candidate=buildRandomTestCase(graph,appPackage);
            Log.d("TFG", "Choosen actions: " + candidate);
            eval=objective.evaluate(candidate, appPackage);
            Log.d("TFG", "Eval: " + eval);
            if(eval>currentBestEval){
                currentBestEval=eval;
                result=candidate;
            }
            i++;
        }
        return result;
    }

    private TestCase buildRandomTestCase(INAGraph graph,String app) throws UiObjectNotFoundException {
        graph.reset();
        List<Action> beforeActions=new ArrayList<>();
        beforeActions.add(new StartAppAction(app));
        List<Action> afterActions=new ArrayList<>();
        afterActions.add(new CloseAppAction(app));
        List<Action> testActions=new ArrayList<>();
        List<Action> candidateActions=null;
        Action chosenAction=null;
        while(testActions.size()<actionsLength && graph.getAvailableActions().size() > 0){
            candidateActions=graph.getAvailableActions();
            Integer selectedActionIndex = getRandom().nextInt(candidateActions.size());
            chosenAction=candidateActions.get(selectedActionIndex);
            testActions.add(chosenAction);
            graph.fictitiousPerformAction(chosenAction);
        }
        return new TestCase(app, Collections.EMPTY_SET,beforeActions,testActions,afterActions);
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


}