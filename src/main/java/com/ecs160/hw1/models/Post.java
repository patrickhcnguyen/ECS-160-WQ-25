package com.ecs160.hw1.models;
import java.util.List;
import java.time.LocalDateTime;

public class Post {
    // values
    private String uri;
    private String cid;
    private Author author;
    private Record record;
    private int replyCount;
    private int repostCount;
    private int likeCount;
    private int quoteCount;
    private String indexedAt;
    private List<Post> replies;
    private String content;
    private LocalDateTime timestamp;

    // getters and setters
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

    public List<Post> getReplies() { return replies; };
    public void setReplies(List<Post> replies) { this.replies = replies; }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() { 
        return content; 
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
