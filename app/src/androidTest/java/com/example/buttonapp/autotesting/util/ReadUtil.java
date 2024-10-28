package com.example.buttonapp.autotesting.util;

import android.os.Environment;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.StartAppAction;
import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.inagraph.actions.RadioButtonAction;
import com.example.buttonapp.autotesting.inagraph.actions.TextInputAction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.buttonapp.autotesting.inagraph.CloseAppAction;
import com.example.buttonapp.autotesting.inagraph.actions.ButtonAction;
import com.example.buttonapp.autotesting.inagraph.actions.CheckBoxAction;
import com.example.buttonapp.autotesting.inagraph.actions.RadioButtonInputGenerator;
import com.example.buttonapp.autotesting.inagraph.actions.TextInputGenerator;

public class ReadUtil {
    String path;
    Boolean sameSeed;

    public ReadUtil(String path, Boolean sameSeed){
        this.path = path;
        this.sameSeed = sameSeed;
    }

    public String getPath(){
        return this.path;
    }

    public String readText(){
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                            + "/" + getPath()));
            String line;
            while ((line = br.readLine())!= null){
                text.append(line);
                text.append("\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public TestCase generateTestCase(){
        List<Action> beforeActions = new ArrayList<>();
        List<Action> afterActions = new ArrayList<>();
        List<Action> testActions = new ArrayList<>();
        String text = readText();
        String[] lines = text.split("\n");
        String appPackage = lines[0];
        Long seed;
        if(sameSeed){
            seed = new Long(lines[1]);
        } else {
            seed = new Random().nextLong();
        }
        Random random = new Random(seed);
        String action = "";
        for(int i = 2; i< lines.length; i++){
            action = lines[i];
            testActions.add(generateActionFromString(action, random.nextInt()));
        }
        beforeActions.add(new StartAppAction(appPackage));
        afterActions.add(new CloseAppAction(appPackage));

        return new TestCase(appPackage, Collections.EMPTY_SET,beforeActions,testActions,afterActions);
    }

    private Action generateActionFromString(String action, Integer seed){
        String[] splitAction = action.split(",");
        String type = splitAction[0];
        String resourceId = splitAction[1];
        resourceId = splitAction[1].substring(resourceId.indexOf("=") + 1 ,resourceId.length()-1);
        Action res = null;
        UiObject object = new UiObject(new UiSelector().resourceId(resourceId));
        switch (type){
            case "BUTTON":
                res = new ButtonAction(object);
                break;
            case "TEXT":
                TextInputGenerator textInputGenerator = new TextInputGenerator(seed);
                res = new TextInputAction(object, textInputGenerator);
                break;
            case "CHECKBOX":
                res = new CheckBoxAction(object);
                break;
            case "RADIO_BUTTON":
                RadioButtonInputGenerator radioButtonInputGenerator = new RadioButtonInputGenerator(seed);
                res = new RadioButtonAction(object, radioButtonInputGenerator);
        }
        return res;
    }
}
