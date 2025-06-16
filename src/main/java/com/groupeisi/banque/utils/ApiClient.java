package com.groupeisi.banque.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    private final String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String get(String endpoint) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("GET Request Failed. HTTP Error Code: " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();
        return response.toString();
    }

    public String post(String endpoint, String jsonPayload) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 201 && responseCode != 200) {
            throw new RuntimeException("POST Request Failed. HTTP Error Code: " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();
        return response.toString();
    }

    public String put(String endpoint, String jsonPayload) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("PUT Request Failed. HTTP Error Code: " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();
        return response.toString();
    }

    public String delete(String endpoint) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 204) {
            throw new RuntimeException("DELETE Request Failed. HTTP Error Code: " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();
        return response.toString();
    }
}
