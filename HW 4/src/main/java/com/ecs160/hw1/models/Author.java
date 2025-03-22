package com.ecs160.hw1.models;

import java.util.Objects;

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

    public Author() {
        // No-arg constructor for tests
    }

    public Author(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(did, author.did) &&
               Objects.equals(handle, author.handle) &&
               Objects.equals(displayName, author.displayName) &&
               Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(did, handle, displayName, name);
    }

    //HIIII
}