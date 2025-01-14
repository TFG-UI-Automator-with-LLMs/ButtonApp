package com.example.buttonapp.autotesting.inagraph.actions;

import android.util.Log;

import net.sf.extjwnl.JWNLException;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import com.example.buttonapp.autotesting.dictionary.DictionaryBasedValueGenerator;

public class TextInputGenerator extends InputGenerator {

    private Long seed;
    private String defaultValue;

    public TextInputGenerator(Long seed, String defaultValue){
        this.seed = seed;
        this.setDefaultValue(defaultValue);
    }

    public String generateInput(UiObject object) throws UiObjectNotFoundException {
        String value = getDefaultValue();

        DictionaryBasedValueGenerator dictionary = new DictionaryBasedValueGenerator(1, getSeed());
        try {
            if(getSeed() > 0 || defaultValue == null)
                value = dictionary.generate().toString();
            Log.d("TFG", value);
            object.setText(value);
        } catch (JWNLException e) {
            e.printStackTrace();
        }


        return value;
    }

    public Long getSeed() {
        return seed;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}

