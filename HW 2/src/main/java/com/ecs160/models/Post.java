package com.ecs160.models;

import com.ecs160.persistence.Persistable;
import com.ecs160.persistence.PersistableField;
import com.ecs160.persistence.PersistableId;
import com.ecs160.persistence.PersistableListField;

import java.util.List;
import java.time.LocalDateTime;

@Persistable
public class Post {
    // Required fields for assignment
    @PersistableId
    private Integer postId;

    @PersistableField
    private String postContent;

    @PersistableListField(className = "com.ecs160.models.Post")
    private List<Post> replies;

    // Additional fields from previous implementation
    private String uri;
    private String cid;
    private Author author;
    private Record record;
    private int replyCount;
    private int repostCount;
    private int likeCount;
    private int quoteCount;
    private String indexedAt;
    private LocalDateTime timestamp;

    // Constructors
    public Post() {}

    // getters and setters
    public Integer getPostId() { return postId; }
    public void setPostId(Integer postId) { this.postId = postId; }

    public String getPostContent() { return postContent; }
    public void setPostContent(String postContent) { this.postContent = postContent; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public Record getRecord() { return record; }
    public void setRecord(Record record) { this.record = record; }

    public int getReplyCount() { return replyCount; }
    public void setReplyCount(int replyCount) { this.replyCount = replyCount; }

    public int getRepostCount() { return repostCount; }
    public void setRepostCount(int repostCount) { this.repostCount = repostCount; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getQuoteCount() { return quoteCount; }
    public void setQuoteCount(int quoteCount) { this.quoteCount = quoteCount; }

    public String getIndexedAt() { return indexedAt; }
    public void setIndexedAt(String indexedAt) { this.indexedAt = indexedAt; }

    public List<Post> getReplies() { return replies; }
    public void setReplies(List<Post> replies) { this.replies = replies; }

    public void setContent(String content) {
        this.postContent = content;
    }

    public String getContent() {
        return postContent;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}