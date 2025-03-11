package com.ecs160.hashtag;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class HashtagController {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public HashtagController() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/hashtag")
    public HashtagResponse generateHashtag(@RequestBody HashtagRequest request) {
        try {
            String prompt = "Please generate a single hashtag for this social media post. " +
                    "Respond with ONLY the hashtag (including the # symbol), nothing else: " +
                    request.getPostContent();

            // Create request body for Ollama
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "llama2");
            requestBody.put("prompt", prompt);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Make request to Ollama
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            String response = restTemplate.postForObject(OLLAMA_URL, entity, String.class);
            
            // Parse response
            JsonNode responseJson = objectMapper.readTree(response);
            String hashtag = responseJson.get("response").asText().trim();
            
            // Ensure the response starts with #
            if (!hashtag.startsWith("#")) {
                hashtag = "#" + hashtag;
            }
            
            // Remove any spaces or special characters
            hashtag = hashtag.replaceAll("[^#A-Za-z0-9_]", "");
            
            return new HashtagResponse(hashtag);
        } catch (Exception e) {
            return new HashtagResponse("#bskypost");
        }
    }
}

class HashtagRequest {
    private String postContent;

    public HashtagRequest() {}

    public HashtagRequest(String postContent) {
        this.postContent = postContent;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}

class HashtagResponse {
    private String hashtag;

    public HashtagResponse() {}

    public HashtagResponse(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
} 