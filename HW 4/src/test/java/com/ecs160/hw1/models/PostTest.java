package com.ecs160.hw1.models;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostTest {
    
    @Test
    public void testPostCreationAndGetters() {
        // Create a Post
        Post post = new Post();
        post.setCid("test-cid");
        post.setUri("test-uri");
        post.setContent("This is test content");
        post.setTimestamp(LocalDateTime.of(2023, 6, 1, 12, 0, 0));
        post.setReplyCount(5);
        post.setRepostCount(10);
        post.setLikeCount(15);
        post.setQuoteCount(3);
        post.setIndexedAt("2023-06-01T12:05:00Z");
        
        // Test getters
        assertEquals("CID should match", "test-cid", post.getCid());
        assertEquals("URI should match", "test-uri", post.getUri());
        assertEquals("Content should match", "This is test content", post.getContent());
        assertEquals("Timestamp should match", LocalDateTime.of(2023, 6, 1, 12, 0, 0), post.getTimestamp());
        assertEquals("Reply count should match", 5, post.getReplyCount());
        assertEquals("Repost count should match", 10, post.getRepostCount());
        assertEquals("Like count should match", 15, post.getLikeCount());
        assertEquals("Quote count should match", 3, post.getQuoteCount());
        assertEquals("IndexedAt should match", "2023-06-01T12:05:00Z", post.getIndexedAt());
    }
    
    @Test
    public void testAuthorGetterAndSetter() {
        // Create a Post
        Post post = new Post();
        
        // Create an Author
        Author author = new Author();
        author.setDid("test-did");
        author.setHandle("test-handle");
        
        // Set the author on the post
        post.setAuthor(author);
        
        // Test that the author was set correctly
        assertEquals("Author should be set correctly", author, post.getAuthor());
        assertEquals("Author DID should match", "test-did", post.getAuthor().getDid());
        assertEquals("Author handle should match", "test-handle", post.getAuthor().getHandle());
    }
    
    @Test
    public void testRepliesGetterAndSetter() {
        // Create a Post
        Post post = new Post();
        
        // Create some replies
        List<Post> replies = new ArrayList<>();
        Post reply1 = new Post();
        reply1.setCid("reply-cid-1");
        Post reply2 = new Post();
        reply2.setCid("reply-cid-2");
        
        replies.add(reply1);
        replies.add(reply2);
        
        // Set the replies on the post
        post.setReplies(replies);
        
        // Test that the replies were set correctly
        assertEquals("Replies should be set correctly", replies, post.getReplies());
        assertEquals("Should have 2 replies", 2, post.getReplies().size());
        assertEquals("First reply CID should match", "reply-cid-1", post.getReplies().get(0).getCid());
        assertEquals("Second reply CID should match", "reply-cid-2", post.getReplies().get(1).getCid());
    }
    
    @Test
    public void testGetChildren() {
        // Create a Post
        Post post = new Post();
        
        // Create some replies
        List<Post> replies = new ArrayList<>();
        Post reply1 = new Post();
        reply1.setCid("reply-cid-1");
        Post reply2 = new Post();
        reply2.setCid("reply-cid-2");
        
        replies.add(reply1);
        replies.add(reply2);
        
        // Set the replies on the post
        post.setReplies(replies);
        
        // Test getChildren
        List<SocialMediaComponent> children = post.getChildren();
        assertEquals("Should have 2 children", 2, children.size());
        assertTrue("Children should be instances of Post", children.get(0) instanceof Post);
        assertTrue("Children should be instances of Post", children.get(1) instanceof Post);
    }
    
    @Test
    public void testGetChildrenWithNoReplies() {
        // Create a Post with no replies
        Post post = new Post();
        
        // Test getChildren
        List<SocialMediaComponent> children = post.getChildren();
        assertNotNull("Children list should not be null", children);
        assertTrue("Children list should be empty", children.isEmpty());
    }
} 