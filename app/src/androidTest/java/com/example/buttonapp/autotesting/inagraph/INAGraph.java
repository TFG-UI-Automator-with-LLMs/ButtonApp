package com.example.buttonapp.autotesting.inagraph;

import android.util.Log;

import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.inagraph.actions.Action;

import java.util.List;
import java.util.Set;

public class INAGraph {
    Node currentNode;
    Node initialNode;

    public INAGraph() {
        this(new Node());
    }

    public INAGraph(Node initialNode) {
        this.currentNode = initialNode;
        this.initialNode = currentNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public Node getInitialNode() {
        return initialNode;
    }

    public List<Action> getAvailableActions() {
        return currentNode.getAvailableActions();
    }

    public Set<Action> getOutboundActions() {
        return currentNode.getOutputVertex().keySet();
    }

    public void performAction(Action a) {
        try {
            a.perform();
            if (currentNode.isOutboundAction(a)) {
                currentNode = currentNode.getOutputVertex().get(a);
            }
        } catch (UiObjectNotFoundException e) {
            Log.d("TFG", "UI Object not found: " + a.getTarget() + " - " + e.getMessage());
        }
    }

    public void fictitiousPerformAction(Action a) {
        if (currentNode.isOutboundAction(a)) {
            currentNode = currentNode.getOutputVertex().get(a);
        }

    }

    public void reset() {
        currentNode = initialNode;
    }

    @Override
    public String toString() {
        return currentNode.toString();
    }
}
