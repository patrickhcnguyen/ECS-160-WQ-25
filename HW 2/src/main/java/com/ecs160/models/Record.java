package com.ecs160.models;
import java.util.List;
import java.time.LocalDateTime;

public class Record {
    // vals
    private String createdAt;
    private List<String> langs;
    private String text;
    private String author;
    private double weight;
    private LocalDateTime timestamp;
    private int wordCount;

    // getters and setters
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<String> getLangs() { return langs; }
    public void setLangs(List<String> langs) { this.langs = langs; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() {
        return author;
    }

    public double getWeight() {
        return weight;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}