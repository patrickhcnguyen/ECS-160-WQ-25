package com.ecs160.hashtag;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HashtagController {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/hashtag")
    public HashtagResponse generateHashtag(@RequestBody HashtagRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama2");
            requestBody.put("prompt", "Generate a single relevant hashtag (starting with #) for this social media post. Response should only contain the hashtag: " + request.getPostContent());
            requestBody.put("stream", false);

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            
            JSONObject response = new JSONObject(restTemplate.postForObject(OLLAMA_URL, entity, String.class));
            String hashtag = response.getString("response").trim();
            
            // Clean up the hashtag
            if (!hashtag.startsWith("#")) {
                hashtag = "#" + hashtag.replaceAll("[^a-zA-Z0-9]", "");
            } else {
                hashtag = hashtag.replaceAll("[^#a-zA-Z0-9]", "");
            }
            
            // If something went wrong and we got an empty hashtag
            if (hashtag.length() <= 1) {
                hashtag = "#bskypost";
            }
            
            System.out.println("Generated hashtag: " + hashtag);
            return new HashtagResponse(hashtag);
        } catch (Exception e) {
            System.err.println("Error generating hashtag: " + e.getMessage());
            e.printStackTrace();
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
    private String result;

    public HashtagResponse() {}

    public HashtagResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
} 