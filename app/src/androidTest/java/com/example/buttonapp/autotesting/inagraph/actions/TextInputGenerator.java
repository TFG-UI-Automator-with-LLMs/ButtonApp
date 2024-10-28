package com.example.buttonapp.autotesting.inagraph.actions;

import android.util.Log;

import androidx.test.uiautomator.UiObject;

import com.example.buttonapp.autotesting.dictionary.DictionaryBasedValueGenerator;

public class TextInputGenerator extends InputGenerator {

    Integer seed;

    public TextInputGenerator(Integer seed){
        this.seed = seed;
    }

    public void generateInput(UiObject object) {
        try {
            DictionaryBasedValueGenerator dictionary = new DictionaryBasedValueGenerator(1, seed);
            String text = dictionary.generate().toString();
            Log.d("TFG", text);
            object.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
