package com.example.buttonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button buttonChangeText;
    private Button buttonSaveText;
    private SharedPreferences sharedPreferences;

    public static final String PREFS_NAME = "MyPrefs";
    private static final String TEXT_KEY = "savedText";
    private static final int REQUEST_MANAGE_EXTERNAL_STORAGE = 2296;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request storage permissions
        requestStoragePermission();

        editText = findViewById(R.id.editText);
        buttonChangeText = findViewById(R.id.buttonChangeText);
        buttonSaveText = findViewById(R.id.buttonSaveText);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved text
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
                Log.d("ButtonSaveText", "Texto guardado: " + textToSave);
            }
        });
    }
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 o superior
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE);
                }
            }
        } else {
            // Android 10 o inferior
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_MANAGE_EXTERNAL_STORAGE);
            }
        }
    }

    // Manejar la respuesta del permiso
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MANAGE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "Permiso de almacenamiento otorgado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
