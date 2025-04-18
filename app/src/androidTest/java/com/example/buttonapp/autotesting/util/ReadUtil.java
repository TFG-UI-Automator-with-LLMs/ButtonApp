package com.example.buttonapp.autotesting.util;

import android.os.Environment;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.inagraph.StartAppAction;
import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.inagraph.actions.LLMInputAction;
import com.example.buttonapp.autotesting.inagraph.actions.LLMInputGenerator;
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

import com.example.buttonapp.autotesting.inagraph.actions.CountDownAction;
import com.example.buttonapp.autotesting.inagraph.actions.ScrollDownAction;
import com.example.buttonapp.autotesting.inagraph.actions.ScrollUpAction;

import android.util.Log;

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
            seed = Math.abs(new Random().nextLong());
        }
        Integer actionsSize = new Integer(lines[2]);
        Random random = new Random(seed);
        String action = "";
        for(int i = 3; i<= actionsSize + 2; i++){
            action = lines[i];
            if (seed <  0)
                testActions.add(generateActionFromString(action,  seed));
            else
                testActions.add(generateActionFromString(action, random.nextLong()));
            if(i == actionsSize+2){
                actionsSize = i;
                break;
            }
        }
        String predicate = lines[actionsSize+1];
        List<String> initialLabels = new ArrayList<>();
        /*
        String initialState = lines[actionsSize+2].replaceAll("\\[", "").replaceAll("\\]", "");
        String finalState = lines[actionsSize+3].replaceAll("\\[", "").replaceAll("\\]", "");
        for (String label: initialState.split(", ")) {
            initialLabels.add(label);
        }*/
        List<String> finalLabels = new ArrayList<>();
        /*for (String label: finalState.split(", ")) {
            finalLabels.add(label);
        }*/
        beforeActions.add(new StartAppAction(appPackage));
        afterActions.add(new CloseAppAction(appPackage));
        TestCase testCase = new TestCase(appPackage, Collections.EMPTY_SET,beforeActions,testActions,afterActions, initialLabels, finalLabels);
        testCase.setPredicate(predicate);
        return testCase;
    }

    public Action generateActionFromString(String action, Long seed) {
        // Dividir la cadena por comas
        String[] splitAction = action.split(",");
        String type = splitAction[0];       // "LLMTEXTINPUT", "TEXT", "BUTTON", etc.
        String resourceId = splitAction[1]; // UiSelector[...]
        // Si no hay valor adicional, usar cadena vacía
        String value = splitAction.length == 2 ? "" : splitAction[2];

        // Extraer el selector
        String selectorType = resourceId.substring(resourceId.indexOf("[")+1, resourceId.indexOf("=")).trim();
        resourceId = resourceId.substring(resourceId.indexOf("=") + 1, resourceId.length()-1);

        // Crear el UiObject
        UiObject object = null;
        if (selectorType.equals("RESOURCE_ID")) {
            object = new UiObject(new UiSelector().resourceId(resourceId));
        } else if (selectorType.equals("DESCRIPTION")) {
            object = new UiObject(new UiSelector().descriptionContains(resourceId));
        } else if (selectorType.equals("TEXT")) {
            object = new UiObject(new UiSelector().textContains(resourceId));
        } else if (selectorType.equals("SCROLLABLE")) {
            object = new UiObject(new UiSelector().scrollable(!type.equals("SCROLL_DOWN")));
        }

        // Quitar comillas del value si vienen en la línea
        String cleanedValue = removeSurroundingQuotes(value.trim());

        Action res = null;
        switch (type) {
            case "BUTTON":
                res = new ButtonAction(object);
                break;
            case "TEXT":
                TextInputGenerator textInputGenerator = new TextInputGenerator(seed, cleanedValue);
                res = new TextInputAction(object, textInputGenerator);
                break;
            case "CHECKBOX":
                res = new CheckBoxAction(object);
                break;
            case "RADIO_BUTTON":
                RadioButtonInputGenerator radioButtonInputGenerator = new RadioButtonInputGenerator(seed);
                res = new RadioButtonAction(object, radioButtonInputGenerator);
                break;
            case "SCROLL_DOWN":
                res = new ScrollDownAction(object);
                break;
            case "SCROLL_UP":
                res = new ScrollUpAction(object);
                break;
            case "COUNT_DOWN":
                res = new CountDownAction(object);
                break; // IMPORTANTE: evitar el salto al siguiente case
            case "LLMTEXTINPUT":
                // Aquí se usa cleanedValue como prompt
                LLMInputGenerator llmInputGenerator = new LLMInputGenerator(seed, cleanedValue);
                res = new LLMInputAction(object, llmInputGenerator);
                break;
            default:
                // Caso por defecto para logs o manejo de errores
                Log.e("ISA", "Tipo de acción no reconocido: " + type);
                break;
        }

        // Solo establecer el value si res no es null
        if (res != null) {
            res.setValue(cleanedValue);
        } else {
            Log.e("ISA", "La acción resultó nula, se omite setValue");
        }

        return res;
    }


    // Método auxiliar para eliminar comillas iniciales y finales
    private String removeSurroundingQuotes(String input) {
        if (input == null) return "";
        if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
            return input.substring(1, input.length() - 1).trim();
        }
        return input.trim();
    }


    public static Action generateActionFromSimpleString(String action, Long seed){
        Log.d("ISA", action);
        String value = null;
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
                TextInputGenerator textInputGenerator = new TextInputGenerator(seed, value);
                res = new TextInputAction(object, textInputGenerator);
                break;
            case "CHECKBOX":
                res = new CheckBoxAction(object);
                break;
            case "RADIO_BUTTON":
                RadioButtonInputGenerator radioButtonInputGenerator = new RadioButtonInputGenerator(seed);
                res = new RadioButtonAction(object, radioButtonInputGenerator);
                break;
            case "SCROLL_DOWN":
                res = new ScrollDownAction(object);
                break;
            case "SCROLL_UP":
                res = new ScrollUpAction(object);
                break;
            case "COUNT_DOWN":
                res = new CountDownAction(object);
            case "LLMTEXTINPUT":
                LLMInputGenerator llmInputGenerator = new LLMInputGenerator(seed, value); //ver si hay q cambiar esto pa pasarle la prompt
                res = new LLMInputAction(object, llmInputGenerator);
        }
        return res;
    }
}
