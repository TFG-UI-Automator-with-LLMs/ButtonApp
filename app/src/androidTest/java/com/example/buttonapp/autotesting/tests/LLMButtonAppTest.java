package com.example.buttonapp.autotesting.tests;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.algorithms.LLMRandomSearch;
import com.example.buttonapp.autotesting.inagraph.INAGraph;
import com.example.buttonapp.autotesting.inagraph.INAGraphBuilder;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ApplicationCrashObjectiveFunction;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ObjectiveFunction;
import com.example.buttonapp.autotesting.util.WriterUtil;

import org.junit.Test;

public class LLMButtonAppTest {
    private void LLMRandomSearchTemplate(String appPackageName, ObjectiveFunction goalFunction, Integer numIterations, Integer actionsLength, String prompt) throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        grantManageAllFilesPermission(device);
        INAGraph graph = INAGraphBuilder.getInstance().build(device, appPackageName, prompt);
        LLMRandomSearch algorithm = new LLMRandomSearch(goalFunction, numIterations, actionsLength, prompt); // pasar aqui la prompt
        TestCase testCase = algorithm.run(graph, appPackageName);
        Log.d("TFG","Test case found: "+testCase);
        Log.d("TFG","Runnig it...");
        testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();
        Log.d("TFG","Done!");
        saveTestCaseFile(appPackageName, -1, testCase, prompt);
    }
    @Test
    public void testSimpleAppCrash() throws UiObjectNotFoundException{
        String appPackageName = "com.example.buttonapp";
        String prompt = "JSON: Genera una lista con 10 direcciones de prueba de calles reales en Sevilla para un campo de tipo Input. Cada dirección debe incluir el nombre de la calle y el número. Da el resultado sin añadir ```json antes de [ al comienzo.";
        ObjectiveFunction goalFunction = new ApplicationCrashObjectiveFunction();
        Integer numIterations = 10;
        Integer actionsLength = 10;
        LLMRandomSearchTemplate(appPackageName, goalFunction, numIterations, actionsLength, prompt); //pasarle aqui la prompt
    }
    private void grantManageAllFilesPermission(UiDevice device) throws UiObjectNotFoundException {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                .setData(Uri.parse("package:" + "com.example.buttonapp"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
                .startActivity(intent);

        // El switch esta en Ajustes
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.waitForIdle(2000);

        // Buscar switchWidget, puede estar en com.android.settings:id/ o android:id/
        UiObject switchWidget = device.findObject(
                new UiSelector().resourceId("com.android.settings:id/switchWidget")
        );
        if (switchWidget.exists() && switchWidget.isEnabled()) {
            switchWidget.click();
            // Hacer click en el allow access
            UiObject allowButton = device.findObject(new UiSelector().textMatches("(?i)allow|permitir"));
            if (allowButton.exists()) {
                allowButton.click();
            }
        }

        // 4) volver hacia atrás
        UiObject navUp = device.findObject(new UiSelector().description("Navigate up"));
        if (navUp.exists()) {
            navUp.click();
        } else {
            // alternativa
            device.pressBack();
        }
    }


    private void saveTestCaseFile(String appPackageName, int seed, TestCase testCase, String prompt) {
        WriterUtil writer = new WriterUtil();
        StringBuilder resultContent = new StringBuilder();

        // Escribir paquete en cabecera
        resultContent.append(appPackageName).append("\n");

        // Escribir semilla o -1 si no se maneja
        resultContent.append(seed).append("\n");

        String testCaseToString = testCase.toString().replace("Test Case[4]:", "").trim();

        // Separar las acciones segun el tipo
        // BUTTON, TEXT, CHECKBOX, RADIO_BUTTON, START, STOP, GO_BACK, SCROLL_DOWN, SCROLL_UP, COUNT_DOWN, LLMTEXTINPUT
        String[] actions = testCaseToString.split("(?=BUTTON|SCROLL_DOWN|LLMTEXTINPUT)");

        // Indicar numero de pasos
        resultContent.append(actions.length).append("\n");

        // Dar formato a las acciones
        for (String action : actions) {
            action = action.trim();
            if (action.equals("TEXT")){
                action.replace("TEXT", "LLMTEXTINPUT");
            }

            if (action.startsWith("LLMTEXTINPUT")) {
                String[] parts = action.split("UiSelector");
                if (parts.length == 2) {
                    resultContent.append("LLMTEXTINPUT, UiSelector").append(parts[1].trim()).append(", \"").append(prompt).append("\"\n"); //[VALOR] puede formatearse para que se incluyan nuevos datos
                } else {
                    // Si no se parsea que escriba algo para evitar fallos
                    resultContent.append("LLMTEXTINPUT, ").append(action).append("\n");
                }
            } else if (action.startsWith("BUTTON")) {
                resultContent.append(action).append("\n");
            } else if (action.startsWith("SCROLL_DOWN")) {
                resultContent.append(action).append("\n");
            } else {
                String[] parts = action.split("UiSelector");
                if (parts.length == 2) {
                    resultContent.append("LLMTEXTINPUT, UiSelector").append(parts[1].trim()).append(", \"").append(prompt).append("\"\n");
                }
            }
        }
        // Añadir la verificacion del estado final
        resultContent.append("finalState.contains(testActions[1].value)\n");

        writer.write(resultContent.toString());
        Log.d("TFG", "Resultado guardado en: " + writer.getPath());
    }

}
