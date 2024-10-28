package com.example.buttonapp.autotesting.diversityActionSelection;

import java.util.List;
import java.util.Random;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.actions.Action;

public interface ActionSelection {
    public Action selectAction(List<TestCase> testCases, List<Action> testCaseActions, List<Action> availableActions);
    public Random getRandom();
    public void setRandom(Random random);
}
