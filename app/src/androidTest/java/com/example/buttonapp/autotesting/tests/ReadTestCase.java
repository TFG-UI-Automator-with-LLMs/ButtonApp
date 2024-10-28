package com.example.buttonapp.autotesting.tests;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.util.ReadUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReadTestCase {

    @Test
    public void read() throws UiObjectNotFoundException {
        UiDevice.getInstance(getInstrumentation());
        //aqui se pone el txt del storage de la app, se almacenan en la raiz de la memoria
        ReadUtil readUtil = new ReadUtil("TestCase-20190227_183604.txt", true);
        TestCase testCase = readUtil.generateTestCase();
        Log.d("TFG","Test case found: "+testCase);
        Log.d("TFG","Runnig it...");
        testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();
        Log.d("TFG","Done!");
    }
}
