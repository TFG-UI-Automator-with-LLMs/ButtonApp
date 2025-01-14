package com.example.buttonapp.autotesting.tests;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.Manifest;

import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.util.ReadUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReadTestCase {
    @Before
    public void setup(){
        requestStoragePermission();
    }
    @Test
    public void read() throws UiObjectNotFoundException {
        UiDevice.getInstance(getInstrumentation());
        //aqui se pone el txt del storage de la app, se almacenan en la raiz de la memoria
        ReadUtil readUtil = new ReadUtil("TestCase-20250114_114600.txt", true);
        TestCase testCase = readUtil.generateTestCase();
        Log.d("TFG","Test case found: "+testCase);
        Log.d("TFG","Runnig it...");
        testCase.executeBefore();
        testCase.executeTest();
        testCase.executeAfter();
        Log.d("TFG","Done!");
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 o superior
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            if (!Environment.isExternalStorageManager()) {
                try {
                    device.executeShellCommand("appops set " + getInstrumentation().getTargetContext().getPackageName() + " MANAGE_EXTERNAL_STORAGE allow");
                    Log.d("Permisos", "Permiso MANAGE_EXTERNAL_STORAGE concedido.");
                } catch (Exception e) {
                    Log.e("Permisos", "Error concediendo permiso MANAGE_EXTERNAL_STORAGE: " + e.getMessage());
                }
            }
        } else {
            // Android 10 o inferior
            if (ContextCompat.checkSelfPermission(getInstrumentation().getTargetContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                try {
                    UiDevice device = UiDevice.getInstance(getInstrumentation());

                    device.executeShellCommand("pm grant " + getInstrumentation().getTargetContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
                    device.executeShellCommand("pm grant " + getInstrumentation().getTargetContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");

                    Log.d("Permisos", "Permisos de lectura y escritura concedidos.");
                } catch (Exception e) {
                    Log.e("Permisos", "Error concediendo permisos: " + e.getMessage());
                }
            }
        }
    }
}
