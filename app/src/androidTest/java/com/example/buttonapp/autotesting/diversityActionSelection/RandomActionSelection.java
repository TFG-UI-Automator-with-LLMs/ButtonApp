package com.example.buttonapp.autotesting.diversityActionSelection;


import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.actions.Action;

import java.util.List;
import java.util.Random;

public class RandomActionSelection implements ActionSelection {
    private Random random;

    @Override
    public Action selectAction(List<TestCase> testCases, List<Action> testCaseActions, List<Action> availableActions) {
        return availableActions.get(getRandom().nextInt(availableActions.size()));
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
}
