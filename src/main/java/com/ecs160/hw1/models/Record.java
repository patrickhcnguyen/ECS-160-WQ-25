package com.ecs160.hw1.models;
import java.util.List;

public class Record {
    // vals
    private String createdAt;
    private List<String> langs;
    private String text;

    // getters and setters
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<String> getLangs() { return langs; }
    public void setLangs(List<String> langs) { this.langs = langs; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
