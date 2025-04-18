package com.example.buttonapp.autotesting.inagraph.actions;

import android.util.Log;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import com.example.buttonapp.BuildConfig;

public class LLMInputGenerator extends InputGenerator {
    private Long seed;
    private String prompt;
    private final String api_key = BuildConfig.API_KEY; //en un .env

    public LLMInputGenerator(Long seed, String prompt) {
        this.seed = seed;
        this.setPrompt(prompt);
    }

    @Override
    public String generateInput(UiObject object) throws UiObjectNotFoundException {
        String res = "";
        try {
            if (api_key != null && !api_key.isEmpty()) {
                if (prompt == null || prompt.trim().isEmpty()) {
                    // Obtenemos la "hint" o contentDescription del EditText
                    String hint = object.getContentDescription();
                    if (hint == null || hint.trim().isEmpty()) {
                        hint = "Introduce algún texto sobre equipos de fubtol";
                    }
                    // En caso de no dar prompt y ejecutarse LLMINPUTTEXT como accion
                    String hintBasedPrompt =
                            "JSON: Genera una lista de 10 ejemplos basados en la pista: \"" +
                                    hint +
                                    "\". Da el resultado sin añadir ```json antes de [ al comienzo.";
                    res = LLMClient.generateResponse(hintBasedPrompt, api_key);

                } else {
                    // si tenemos prompt simplemente se le pasa
                    res = LLMClient.generateResponse(prompt, api_key);
                }
            }
            object.setText(res);

        } catch (Exception e) {
            e.printStackTrace();
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
