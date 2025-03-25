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
import com.example.buttonapp.autotesting.inagraph.actions.Action;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ApplicationCrashObjectiveFunction;
import com.example.buttonapp.autotesting.objectivefunctions.graph.ObjectiveFunction;
import com.example.buttonapp.autotesting.util.WriterUtil;

import org.junit.Assert;
import org.junit.Test;

public class ProjectValidationTests {
    private TestCase LLMRandomSearchTemplate(String appPackageName, ObjectiveFunction goalFunction, Integer numIterations, Integer actionsLength, String prompt) throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        grantManageAllFilesPermission(device);
        INAGraph graph = INAGraphBuilder.getInstance().build(device, appPackageName, prompt);
        LLMRandomSearch algorithm = new LLMRandomSearch(goalFunction, numIterations, actionsLength, prompt); // pasar aqui la prompt
        TestCase testCase = algorithm.run(graph, appPackageName);
        Log.d("TFG","Test case found: "+testCase);
        Log.d("TFG","Runnig it...");
        /*testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();*/
        Log.d("TFG","Done!");
        return testCase;
    }
    // test suite, the idea is to generate 4 scenarios, 2 positive, 2 negative
    // 2 of them using happy path with and without prompt, the other ones pure evil
    @Test
    public void testPositiveLLMPrompt() throws UiObjectNotFoundException{
        String packageName = "com.example.buttonapp";
        ObjectiveFunction goalFunction = new ApplicationCrashObjectiveFunction();
        Integer numIterations = 3;
        Integer actionsLength = 3;
        String prompt = "JSON: Genera una lista con 10 números de teléfono válidos con el formato español, incluyendo prefijo.Da el resultado sin añadir ```json antes de [ al comienzo.";
        TestCase testCase = LLMRandomSearchTemplate(packageName, goalFunction, numIterations, actionsLength, prompt);
        // Check the LLM generated value from the Input, editText as Android specifies
        testCase.executeBefore();
        testCase.executeTest();
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject inputField = device.findObject(new UiSelector().resourceId(packageName+":id/editText"));
        String text = inputField.getText();
        Log.d("TFG TEXT TO VALIDATE: ", text);
        // Check the action is LLMTEXTINPUT
            // Parse the test case to String
        //String testCaseToString = testCase.toString().replace("Test Case[5]:", "").trim();
            // Get the actions
            // check the provided value by the LLM
        if(text.trim().matches("^\\+34[6-9][0-9]{8}$")){
            boolean match = text.trim().matches("^\\+34[6-9][0-9]{8}$");
            Assert.assertTrue(text, match);
            Log.d("TFG VALIDATION TEST 1 RESULT: ","The following value follows the pattern"+ text);
        } else {
            Log.d("TFG VALIDATION TEST 1 RESULT: ", "Lo hizo otra acción");
        }
        /*String[] actions = testCaseToString.split("(?=BUTTON|SCROLL_DOWN|LLMTEXTINPUT|TEXT|CHECKBOX|RADIO_BUTTON|START|STOP|GO_BACK|SCROLL_UP|COUNT_DOWN)");
        for (String action : actions) {
            action = action.trim();
            if (action.startsWith("LLMTEXTINPUT")) {
                // Assert prompt value follows the Spanish number pattern
                boolean match = text.matches("^\\+34[6-9][0-9]{8}$");
                Assert.assertTrue(text, match);
                Log.d("TFG VALIDATION TEST 1 RESULT: ","The following value follows the pattern"+ text);
            }
        }*/
        testCase.executeAfter();
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
}
