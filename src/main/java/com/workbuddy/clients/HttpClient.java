package com.workbuddy.clients;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.workbuddy.models.ChatGPTRequestBody;
import com.workbuddy.models.Message;
import com.workbuddy.util.Constants;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpClient {
    public String makeRequestToChatGPT(String urlString, String method, ChatGPTRequestBody requestBody) throws IOException, ParseException {
        // Set the API endpoint URL
        URL url = new URL(urlString);

        // Create an HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method
        connection.setRequestMethod(method);

        // Set the Content-Type and Authorization headers
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + System.getProperty("apiKey"));

        // Enable output and input streams
        connection.setDoOutput(true);
        connection.setDoInput(true);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(requestBody);
        // Write the request body
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        // Get the response
        boolean isError =false;
        int responseCode = connection.getResponseCode();
        BufferedReader reader;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            isError =true;
        }

        // Read the response
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        if(isError){
            System.out.println(response);
            throw new RuntimeException("Bad response");
        }
        String content = parseAndGetContent(response.toString());

        // Close the connection
        connection.disconnect();
        return content;
    }

    private String parseAndGetContent(String response) throws ParseException {
        JSONObject resp = (JSONObject) new JSONParser().parse(response);
        JSONArray choices = (JSONArray) resp.get("choices");
        JSONObject choice = (JSONObject) choices.get(0);
        JSONObject message = (JSONObject) choice.get("message");
        String content = message.get("content").toString();
        return content;
    }

}
