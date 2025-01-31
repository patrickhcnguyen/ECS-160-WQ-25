package com.ecs160.hw1.models;

public class Author {
    // vals

    private String did;
    private String handle;
    private String displayName;
    private String name;

    // getters and setters
    public String getDid() { return did; }
    public void setDid(String did) { this.did = did; }

    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getName() { 
        return name; 
    }

    public Author(String name) {
        this.name = name;
    }

    //HIIII
}