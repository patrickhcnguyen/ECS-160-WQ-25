package com.ecs160.moderation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
public class ModerationController {
    private static final List<String> BANNED_WORDS = Arrays.asList(
            "illegal", "fraud", "scam", "exploit", "dox",
            "swatting", "hack", "crypto", "bots"
    );

    @PostMapping("/moderate")
    public ModerationResponse moderate(@RequestBody ModerationRequest request) {
        String content = request.getPostContent().toLowerCase();
        
        // Check for banned words
        for (String bannedWord : BANNED_WORDS) {
            if (content.contains(bannedWord)) {
                return new ModerationResponse("FAILED");
            }
        }
        
        // If no banned words found, forward to hashtag service
        try {
            RestTemplate restTemplate = new RestTemplate();
            HashtagRequest hashtagRequest = new HashtagRequest(request.getPostContent());
            HashtagResponse response = restTemplate.postForObject(
                "http://localhost:30002/hashtag",
                hashtagRequest,
                HashtagResponse.class
            );
            return new ModerationResponse(response.getResult());
        } catch (Exception e) {
            return new ModerationResponse("#bskypost");
        }
    }
}

class ModerationRequest {
    private String postContent;

    public ModerationRequest() {}

    public ModerationRequest(String postContent) {
        this.postContent = postContent;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}

class ModerationResponse {
    private String result;

    public ModerationResponse() {}

    public ModerationResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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