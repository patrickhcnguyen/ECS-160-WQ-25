package com.ecs160.main;

import com.ecs160.models.Post;
import com.ecs160.utils.parser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SpringBootApplication
public class MainApplication {
    private static final String MODERATION_URL = "http://localhost:30001/moderate";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            try {
                // Read input.json using your existing parser
                File jsonFile = new File("src/main/resources/input.json");
                FileInputStream inputStream = new FileInputStream(jsonFile);
                List<Post> posts = parser.parse(inputStream);
                
                System.out.println("Successfully parsed " + posts.size() + " posts");

                // Process each post and its replies
                for (Post post : posts) {
                    String postResult = processContent(post.getPostContent());
                    System.out.println(postResult.equals("FAILED") ? 
                        "[DELETED]" : 
                        post.getPostContent() + " " + postResult);

                    if (post.getReplies() != null) {
                        for (Post reply : post.getReplies()) {
                            String replyResult = processContent(reply.getPostContent());
                            System.out.println("--> " + (replyResult.equals("FAILED") ? 
                                "[DELETED]" : 
                                reply.getPostContent() + " " + replyResult));
                        }
                    }
                    System.out.println(); // Empty line between posts
                }
            } catch (Exception e) {
                System.err.println("Error processing posts: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private String processContent(String content) {
        try {
            System.out.println("Processing content: " + content);
            ModerationRequest request = new ModerationRequest(content);
            ModerationResponse response = restTemplate.postForObject(MODERATION_URL, request, ModerationResponse.class);
            String result = response != null ? response.getResult() : "#bskypost";
            System.out.println("Result: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error processing content: " + e.getMessage());
            e.printStackTrace();
            return "#bskypost";
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