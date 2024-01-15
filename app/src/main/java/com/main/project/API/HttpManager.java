package com.main.project.API;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpManager {

    public static String getData(String URL){
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line+'\n');
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        }catch(Exception ex){
            Log.d("HttpURLConnection", ex.toString());
        }
        return null;
    }

    public static String postData(String targetUrl, String jsonData) {
        try {
            // Create a URL object with the target URL
            URL url = new URL(targetUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the request method to "POST"
            connection.setRequestMethod("POST");

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable input/output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Prepare the JSON data
            byte[] requestBodyBytes = jsonData.getBytes(StandardCharsets.UTF_8);

            // Write the JSON data to the output stream
            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                os.write(requestBodyBytes);
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read and handle the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    return responseBuilder.toString();
                }
            } else {
                // Handle the error response if needed
                System.out.println("Error response: " + responseCode);
                return null;
            }
        } catch (Exception ex) {
            Log.d("HttpURLConnection", ex.toString());
        }
        return null;
    }
}
