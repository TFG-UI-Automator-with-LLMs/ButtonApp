package com.example.buttonapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.util.Log; // Importar Log

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button buttonChangeText;
    private Button buttonSaveText;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String TEXT_KEY = "savedText";

    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        buttonChangeText = findViewById(R.id.buttonChangeText);
        buttonSaveText = findViewById(R.id.buttonSaveText);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Cargar texto guardado al iniciar
        String savedText = sharedPreferences.getString(TEXT_KEY, "");
        editText.setText(savedText);

        buttonChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("Texto cambiado!");
            }
        });

        buttonSaveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToSave = editText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, textToSave);
                editor.apply();
                Log.d("ButtonSaveText", "Texto guardado: " + textToSave); // Log para depuración
            }
        });
    }
}
