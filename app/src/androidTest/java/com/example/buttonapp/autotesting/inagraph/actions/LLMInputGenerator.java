package com.example.buttonapp.autotesting.inagraph.actions;

import android.util.Log;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import com.example.buttonapp.BuildConfig;

public class LLMInputGenerator extends InputGenerator{
    private Long seed;
    private String prompt;
    private final String api_key = BuildConfig.API_KEY; //meterla en un .env

    public LLMInputGenerator(Long seed, String prompt) {
        this.seed = seed;
        this.setPrompt(prompt);
    }

    @Override
    public String generateInput(UiObject object) throws UiObjectNotFoundException {
        String res = ""; //resultado de la prompt
        try {
            if (api_key != null && !api_key.isEmpty() && prompt !=null && !prompt.isEmpty()){
                res = LLMClient.generateResponse(prompt, api_key);
            }
            Log.d("TFG LLMInput", res);
            object.setText(res);
        } catch (Exception e) {
            e.printStackTrace(); //gestion en caso de error al realizar la peticion
        }

        return res;
    }

    public Long getSeed() {
        return seed;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getApi_key() {
        return api_key;
    }
}
