package com.example.buttonapp.autotesting.inagraph.actions;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class LLMClient {
    private static volatile String firstGeneratedValue="";
    private static volatile String generatedValue=""; //propiedad para comprobar en la validacion en tiempo de ejecución
    // Metodo para generar la prompt y obtener los valores
    public static String generateResponse(String prompt, String apiKey) throws Exception {
        // Se usa el modelo gemini-2.0-flash, se puede cambiar a otro según se requiera.
        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST"); //hacemos post con la prompt y despues gestionamos la respuesta
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000); //10 seg de timeout, esto depende de la prompt que realicemos
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        // Construir el cuerpo de la petición JSON, siguiente formato
        /*
        * {
              "contents": [
                {
                  "parts": [
                    {
                      "text": "Genera valores con campos válidos de calles de Sevilla para introducirlos en un campo de tipo Input, con 10 está bien"
                    }
                  ]
                }
              ]
            }
        * */
        JSONObject payload = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject contentObject = new JSONObject();
        JSONArray partsArray = new JSONArray();
        JSONObject partObject = new JSONObject();
        partObject.put("text", prompt);
        partsArray.put(partObject);
        contentObject.put("parts", partsArray);
        contentsArray.put(contentObject);
        payload.put("contents", contentsArray);

        String jsonPayload = payload.toString();

        // Enviar la petición POST
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Error en la conexión, código de respuesta: " + responseCode);
        }

        // Leer la respuesta
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Parsear la respuesta JSON para extraer el texto generado
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray candidates = jsonResponse.getJSONArray("candidates");
        if (candidates.length() > 0) {
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            if (parts.length() > 0) {
                JSONObject firstPart = parts.getJSONObject(0);
                Log.d("TFG FIRST PART JSON: ",firstPart.toString());
                String jsonText = firstPart.getString("text").trim();
                Log.d("TFG JSON FORMATED: ",jsonText);
                return extractedValue(jsonText);
            }
        }
        return "";
    }
    //metodo para sacar el valor del campo indicado en el json de la prompt
    private static String extractedValue(String text) throws Exception{
        JSONArray resArray = new JSONArray(text);
        if (resArray.length()>0){
            Random random = new Random();
            int randomIndex = random.nextInt(resArray.length());
            String randomValue = resArray.getString(randomIndex);
            firstGeneratedValue =resArray.getString(0);
            generatedValue = randomValue;
            return randomValue;
        }
        return "";
    }
    //metodo para recuperar el ultimo valor y comprobar en las pruebas de validacion
    public static String getGeneratedValue(){
        return generatedValue;
    }
    public static String getFirstGeneratedValue(){
        return firstGeneratedValue;
    }
    public static void resetGeneratedValues(){
        firstGeneratedValue="";
        generatedValue="";
    }
}

