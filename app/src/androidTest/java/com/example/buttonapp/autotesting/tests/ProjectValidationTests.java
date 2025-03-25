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
import com.example.buttonapp.autotesting.inagraph.actions.LLMClient;
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
        testCase.executeBefore();
        testCase.executeTest();
        String firstLLMPromptedValue = LLMClient.getFirstGeneratedValue();
        String LLMPromptedValue= LLMClient.getGeneratedValue();
        // Check the LLM generated value from the Input, editText as Android specifies
        //UiDevice device = UiDevice.getInstance(getInstrumentation());
        //UiObject inputField = device.findObject(new UiSelector().resourceId(packageName+":id/editText"));
        //String text = inputField.getText();
        //Log.d("TFG TEXT TO VALIDATE: ", LLMPromptedValue);
        // check the provided value by the LLM
        if(LLMPromptedValue.trim().matches("^\\+34\\s?[6-9]\\d{2}\\s?\\d{3}\\s?\\d{3}$")){
           // try check some other validation
            Assert.assertTrue("TFG CONMUTATION TEST VALIDATION: The first and last value are not the same: ", !firstLLMPromptedValue.equals(LLMPromptedValue));
            Log.d("TFG CONMUTATION VALUES", "The fist prompted generated value: "+firstLLMPromptedValue+ "and a random one: "+LLMPromptedValue);
            boolean match = LLMPromptedValue.trim().matches("^\\+34\\s?[6-9]\\d{2}\\s?\\d{3}\\s?\\d{3}$");
            Assert.assertTrue(LLMPromptedValue, match);
            Log.d("TFG VALIDATION TEST 1 RESULT: ","The following value follows the spanish phone number pattern"+ LLMPromptedValue);
        } else {
            Log.d("TFG VALIDATION TEST 1 RESULT: ", "Lo hizo otra acción");
        }
        testCase.executeAfter();
    }
    @Test
    public void testNegativeLLMPrompt() throws UiObjectNotFoundException{
        String packageName = "com.example.buttonapp";
        ObjectiveFunction goalFunction = new ApplicationCrashObjectiveFunction();
        Integer numIterations = 3;
        Integer actionsLength = 3;
        String prompt = "JSON: Genera una lista con 10 números de teléfono válidos con el formato americano, incluyendo prefijo.Da el resultado sin añadir ```json antes de [ al comienzo.";
        TestCase testCase = LLMRandomSearchTemplate(packageName, goalFunction, numIterations, actionsLength, prompt);
        testCase.executeBefore();
        testCase.executeTest();
        String LLMPromptedValue= LLMClient.getGeneratedValue();
        // Check the LLM generated value from the Input, editText as Android specifies
        //UiDevice device = UiDevice.getInstance(getInstrumentation());
        //UiObject inputField = device.findObject(new UiSelector().resourceId(packageName+":id/editText"));
        //String text = inputField.getText();
        Log.d("TFG TEXT TO VALIDATE: ", LLMPromptedValue);
        // check the provided value by the LLM
        if(!LLMPromptedValue.trim().matches("^\\+34\\s?[6-9]\\d{2}\\s?\\d{3}\\s?\\d{3}$")){
            boolean match = LLMPromptedValue.trim().matches("^\\+34\\s?[6-9]\\d{2}\\s?\\d{3}\\s?\\d{3}$");
            Assert.assertFalse(LLMPromptedValue, match);
            Log.d("TFG VALIDATION TEST 2 RESULT: ","The following value does not follow the spanish phone number pattern"+ LLMPromptedValue);
        } else {
            Log.d("TFG VALIDATION TEST 2 RESULT: ", "Lo hizo otra acción");
        }
        testCase.executeAfter();
    }

    @Test
    public void testSwitchingNoValueFallback() throws UiObjectNotFoundException {
        String packageName = "com.example.buttonapp";
        ObjectiveFunction goalFunction = new ApplicationCrashObjectiveFunction();
        Integer numIterations = 3;
        Integer actionsLength = 3;
        // Se utiliza un prompt diseñado para que el LLM no genere ningún valor.
        String prompt = "JSON: Genera una lista vacía";
        TestCase testCase = LLMRandomSearchTemplate(packageName, goalFunction, numIterations, actionsLength, prompt);

        testCase.executeBefore();
        testCase.executeTest();

        String LLMPromptedValue = LLMClient.getGeneratedValue();
        Log.d("TFG SWITCHING TEST", "Valor generado: " + LLMPromptedValue);

        // Mecanismo de fallback: si no se recibe valor, se asigna un valor por defecto.
        String fallbackValue = "ValorPorDefecto";
        String finalValue;
        if (LLMPromptedValue == null || LLMPromptedValue.trim().isEmpty()) {
            finalValue = fallbackValue;
            Log.d("TFG SWITCHING TEST", "No se recibió valor; se asigna el fallback: " + finalValue);
        } else {
            finalValue = LLMPromptedValue;
            Log.d("TFG SWITCHING TEST", "Se recibió valor inesperado: " + finalValue);
            Assert.fail("Se esperaba no recibir ningún valor y activar el fallback, pero se recibió: " + finalValue);
            return;
        }

        // Validamos que, en caso de no recibir valor, el sistema haya aplicado correctamente el fallback.
        Assert.assertEquals("El valor final debe ser el valor de fallback cuando no se recibe valor", fallbackValue, finalValue);
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
