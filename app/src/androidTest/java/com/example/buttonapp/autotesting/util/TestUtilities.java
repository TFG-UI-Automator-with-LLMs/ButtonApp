package com.example.buttonapp.autotesting.util;

import android.os.RemoteException;
import android.widget.EditText;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiCollection;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import java.util.ArrayList;
import java.util.List;

import com.example.buttonapp.autotesting.inagraph.actions.ElementIdentifier;

public class TestUtilities {

    //Cierra las aplicaciones abiertas si las hubiera y vuelve a la página principal.
    public static void CloseRecentApps() throws RemoteException {
        UiDevice mDevice = UiDevice.getInstance();
        mDevice.pressHome();
        mDevice.pressRecentApps();
        try{
            UiObject closeAppsButton = new UiObject(new UiSelector().className("android.widget.Button").description("Cerrar todo"));
            closeAppsButton.click();
        } catch (Exception e){
        }
        mDevice.pressHome();

    }

    //Método para buscar la app en el box de aplicaciones y abrirla.
    public static void OpenApp(String text, boolean horizontalMenu) throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance();
        //Abrimos el box de aplicaciones
        UiObject appsButton = new UiObject(new UiSelector().className("android.widget.TextView").text("Apps"));
        appsButton.clickAndWaitForNewWindow();
        //Obtenemos la lista de aplicaciones y configuramos el scroll para buscar la app
        UiScrollable appList = new UiScrollable(new UiSelector().scrollable(true));
        if (horizontalMenu == true) {
            appList.setAsHorizontalList();
        }
        //Buscamos la app y la abrimos
        UiSelector appSelector = new UiSelector().className("android.widget.TextView").text(text);
        appList.scrollIntoView(appSelector);
        UiObject appIcon = new UiObject(new UiSelector().className("android.widget.TextView").text(text));
        appIcon.clickAndWaitForNewWindow();
    }

    //Método para buscar botones
    public static List<UiObject> findButtons(UiDevice device) throws UiObjectNotFoundException {
        List<UiObject> result = new ArrayList<>();
        BySelector sel = By.clazz("android.widget.Button");
        List<UiObject2> buttons = device.findObjects(sel);
        UiSelector selector = null;
        UiObject button = null;
        for (UiObject2 btn : buttons) {
            selector = new UiSelector().text(btn.getText());
            button = device.findObject(selector);
            result.add(button);
        }
        return result;
    }

    //Método para buscar entradas de texto
    public static List<UiObject> findInputTexts(UiDevice device) throws UiObjectNotFoundException {
        List<UiObject> result = new ArrayList<>();
        UiSelector selector = new UiSelector().className(EditText.class);
        UiCollection collection = new UiCollection(selector);
        for (int i = 0; i < collection.getChildCount(selector); i++)
            result.add(collection.getChildByInstance(selector, i));
        return result;
    }

    public static List<UiObject> findCheckBox(UiDevice mDevice) {
        List<UiObject> buttons = ElementIdentifier.findElements(mDevice, "android.widget.CheckBox");
        return buttons;
    }
}
