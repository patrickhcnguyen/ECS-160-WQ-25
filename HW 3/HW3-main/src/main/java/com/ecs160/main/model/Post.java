package com.ecs160.main.model;

import java.util.List;

public class Post {
    private String text;
    private int likes;
    private List<Reply> replies;

    public Post() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
} 