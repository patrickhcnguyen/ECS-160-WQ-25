package com.ecs160.hw1.decorators;

import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.models.SocialMediaComponent;
import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HashtagDecorator implements SocialMediaComponent {
    private final Post post;
    private static final Map<String, String> hashtags = new HashMap<>();

    public HashtagDecorator(Post post) {
        this.post = post;
    }

    @Override
    public String getContent() {
        return post.getContent();
    }

    @Override
    public List<SocialMediaComponent> getChildren() {
        return post.getChildren();
    }

    @Override
    public LocalDateTime getTimestamp() {
        return post.getTimestamp();
    }

    public void setHashtag(String hashtag) {
        hashtags.put(post.getUri(), hashtag);
    }

    public String getHashtag() {
        return hashtags.getOrDefault(post.getUri(), "No hashtag");
    }

    public static void printHashtags() {
        hashtags.forEach((uri, hashtag) -> 
            System.out.println("Post URI: " + uri + " | Hashtag: " + hashtag));
    }

    public String getUri() { return post.getUri(); }
    public int getLikeCount() { return post.getLikeCount(); }
} 