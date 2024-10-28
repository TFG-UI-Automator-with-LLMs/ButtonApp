package com.example.buttonapp.autotesting.diversityActionSelection;


import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.actions.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class IntelligentActionSelection implements ActionSelection {
    private Random random;
    @Override
    public Action selectAction(List<TestCase> testCases, List<Action> testCaseActions, List<Action> availableActions) {
        return selectAction(countActions(testCases, testCaseActions, availableActions));
    }

    @Override
    public Random getRandom() {
        if(random == null){
            random = new Random();
        }
        return random;
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    private static Map<Action, Integer> invertedValues(Map<Action, Integer> elements){
        Map<Action, Integer> res = new HashMap<>();
        Integer sum = elementsSum(elements, false);
        for(Map.Entry<Action, Integer> entry: elements.entrySet()) {
            res.put(entry.getKey(), sum - (entry.getValue() + 1));
        }
        return res;
    }

    private static Integer elementsSum(Map<Action, Integer> values, boolean inverted) {
        Integer res = 0;
        for (Integer value : values.values()) {
            if(!inverted){
                res = res + value + 1;
            } else {
                res = res + value;
            }

        }
        return res;
    }

    private Action selectAction(Map<Action, Integer> elements) {
        Map<Action, Integer> inverseElements = invertedValues(elements);
        Integer elementsCount = elementsSum(inverseElements,  true);
        Action element = null;
        Integer randomValue = getRandom().nextInt(elementsCount);
        for (Map.Entry<Action, Integer> entry: inverseElements.entrySet()) {
            randomValue -= entry.getValue();
            if(randomValue < 0) {
                element = entry.getKey();
                break;
            }
        }
        return element;
    }

    private Map<Action, Integer> countActions(List<TestCase> testCases, List<Action> testCaseActions, List<Action> availableActions){
        Map<Action, Integer> res = new HashMap<>();
        for (TestCase t: testCases) {
            for(Action a: availableActions){
                if(t.getTestActions().contains(a)){
                    if(res.keySet().contains(a)){
                        res.put(a, res.get(a)+1);
                    } else {
                        res.put(a, 1);
                    }
                }
            }
        }
        for(Action a: availableActions){
            if(testCaseActions.contains(a)){
                if(res.keySet().contains(a)){
                    res.put(a, res.get(a)+1);
                } else {
                    res.put(a, 1);
                }
            }
        }
        for(Action a: availableActions){
            if(!res.keySet().contains(a)){
                res.put(a, 0);
            }
        }
        return res;
    }
}
