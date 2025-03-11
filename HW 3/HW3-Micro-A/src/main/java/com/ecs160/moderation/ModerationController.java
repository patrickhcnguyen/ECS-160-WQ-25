package com.ecs160.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
public class ModerationController {
    private static final List<String> BANNED_WORDS = Arrays.asList(
            "illegal", "fraud", "scam", "exploit", "dox",
            "swatting", "hack", "crypto", "bots"
    );

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/moderate")
    public ModerationResponse moderate(@RequestBody ModerationRequest request) {
        String content = request.getPostContent().toLowerCase(Locale.ROOT);
        
        // Check for banned words
        for (String bannedWord : BANNED_WORDS) {
            if (content.contains(bannedWord.toLowerCase())) {
                return new ModerationResponse("FAILED");
            }
        }

        // If moderation passes, forward to hashtag service
        try {
            String hashtagServiceUrl = "http://localhost:30002/hashtag";
            HashtagResponse hashtagResponse = restTemplate.postForObject(
                    hashtagServiceUrl,
                    new HashtagRequest(request.getPostContent()),
                    HashtagResponse.class
            );
            return new ModerationResponse(hashtagResponse.getHashtag());
        } catch (Exception e) {
            // If hashtag service fails, return a default response
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