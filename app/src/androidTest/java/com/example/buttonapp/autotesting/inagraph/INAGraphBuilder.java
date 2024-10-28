package com.example.buttonapp.autotesting.inagraph;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.inagraph.actions.ActionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class INAGraphBuilder {

    private static INAGraphBuilder _instance;
    private List<Action> executedActions = new ArrayList<>();

    private INAGraphBuilder() {
    }

    public static INAGraphBuilder getInstance() {
        if (_instance == null)
            _instance = new INAGraphBuilder();
        return _instance;
    }

    public INAGraph build(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        startApp(device, appPackage);
        Node node = buildNode(device, appPackage);
        closeApp(device, appPackage);
        return new INAGraph(node);
    }

    private void closeApp(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        CloseAppAction action = new CloseAppAction(appPackage);
        action.perform();
    }

    private void startApp(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        StartAppAction action = new StartAppAction(appPackage);
        action.perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
    }

    public Node buildNode(UiDevice device, String appPackage) throws UiObjectNotFoundException {
        Node node = new Node();
        createActions(node, device);
        buildVertex(node, device, appPackage);
        return node;
    }

    public void createActions(Node node, UiDevice device) {
        Map<UiObject, Action> actions = ActionFactory.createActions(device,  new Random().nextInt());
        node.getControls().addAll(actions.keySet());
        node.getAvailableActions().addAll(actions.values());
    }


    public void buildVertex(Node node, UiDevice device, String appPackage) throws UiObjectNotFoundException {
        for (Action a : node.getAvailableActions()) {
            try {
                executedActions.add(a);
                Boolean crashed;
                String packageName = device.getCurrentPackageName();
                a.perform();
                crashed = !packageName.equals(device.getCurrentPackageName());
                if (!isSameNode(node, device) && !crashed) {
                    Node nextNode = buildNode(device, appPackage);
                    node.getOutputVertex().put(a, nextNode);
                    Action goBack = new GoBackAction(device);
                    nextNode.getOutputVertex().put(goBack, node);
                    goBack.perform();
                }
                executedActions.remove(a);
                if(crashed){
                    closeApp(device, appPackage);
                    startApp(device, appPackage);
                    for(Action action: executedActions){
                        action.perform();
                    }
                }

            } catch (Throwable e) {
                executedActions.remove(a);
                closeApp(device, appPackage);
                startApp(device, appPackage);
                for(Action action: executedActions){
                    action.perform();
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Clearly, this implementation is simplistic and can lead to inconsistent behaviour when applied
     * to complex user interfaces, but for a first approach it could be a starting point.
     *
     * @param currentNode
     * @param device
     * @return Whether we are in a new UI state or not.
     * @throws UiObjectNotFoundException
     */

    public boolean isSameNode(Node currentNode, UiDevice device) throws UiObjectNotFoundException {
        boolean result = true;
        List<Action> actions = new ArrayList<>(ActionFactory.createActions(device, new Random().nextInt()).values());
        for (int i = 0; i < actions.size() && result; i++)
            result = (result && currentNode.getAvailableActions().contains(actions.get(i)));
        result = (result && currentNode.getAvailableActions().size() == (actions.size()));
        return result;
    }

}
