package com.ecs160.main;

import com.ecs160.main.model.Post;
import com.ecs160.main.model.Reply;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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
            // Read and parse input.json
            List<Post> posts = objectMapper.readValue(
                    new File("src/main/resources/input.json"),
                    new TypeReference<List<Post>>() {}
            );

            // Get top 10 most-liked posts
            List<Post> top10Posts = posts.stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()))
                    .limit(10)
                    .collect(Collectors.toList());

            // Process each post and its replies
            for (Post post : top10Posts) {
                String postResult = processContent(post.getText());
                System.out.println(postResult.equals("FAILED") ? "[DELETED]" : post.getText() + " " + postResult);

                if (post.getReplies() != null) {
                    for (Reply reply : post.getReplies()) {
                        String replyResult = processContent(reply.getText());
                        System.out.println("--> " + (replyResult.equals("FAILED") ? "[DELETED]" : reply.getText() + " " + replyResult));
                    }
                }
                System.out.println(); // Empty line between posts
            }
        };
    }

    private String processContent(String content) {
        try {
            ModerationRequest request = new ModerationRequest(content);
            ModerationResponse response = restTemplate.postForObject(MODERATION_URL, request, ModerationResponse.class);
            return response != null ? response.getResult() : "#bskypost";
        } catch (Exception e) {
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