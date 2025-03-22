package com.ecs160.hw1.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class OllamaService {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();
    private static final Gson gson = new GsonBuilder()
        .setLenient()
        .create();

    public String generateHashtag(String content) {
        try {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "llama3");
            requestBody.addProperty("prompt",
                "Generate exactly one hashtag (no explanation, just the hashtag) for: " + content);
            requestBody.addProperty("stream", false);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

            HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());

            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            String hashtag = jsonResponse.get("response").getAsString().trim();
            
            // Clean up the response to get just one hashtag
            if (hashtag.contains("\n")) {
                hashtag = hashtag.split("\n")[0];
            }
            if (!hashtag.startsWith("#")) {
                hashtag = "#" + hashtag.replaceAll("[^a-zA-Z0-9]", "");
            }
            return hashtag;
        } catch (Exception e) {
            System.err.println("Error generating hashtag: " + e.getMessage());
            return "#error";
        }
    }

    public static void main(String[] args) {
        OllamaService service = new OllamaService();
    }
} 