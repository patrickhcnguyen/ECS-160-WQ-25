package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.models.Author;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerTest {
    private List<Post> testPosts;
    private ComprehensiveAnalyzer analyzer;
    
    @Before
    public void setUp() {
        testPosts = createTestPosts();
        analyzer = new ComprehensiveAnalyzer();
    }
    
    @Test
    public void testAnalyzeWithValidPosts() {
        // This test is primarily to ensure that the analyze method doesn't throw exceptions
        // For more detailed analysis testing, we would need to mock the visitors or 
        // check for specific output in the console
        analyzer.analyze(testPosts);
        assertTrue("Analysis should complete without errors", true);
    }
    
    @Test
    public void testAnalyzeWithEmptyPosts() {
        // Test with an empty list
        List<Post> emptyList = new ArrayList<>();
        analyzer.analyze(emptyList);
        assertTrue("Analysis with empty list should complete without errors", true);
    }
    
    private List<Post> createTestPosts() {
        List<Post> posts = new ArrayList<>();
        
        // Create first test post
        Post post1 = new Post();
        post1.setCid("test-cid-1");
        post1.setUri("test-uri-1");
        post1.setContent("This is a test post 1");
        post1.setTimestamp(LocalDateTime.now());
        post1.setLikeCount(10);
        post1.setRepostCount(5);
        
        Author author1 = new Author();
        author1.setDid("test-author-1");
        author1.setHandle("testuser1");
        post1.setAuthor(author1);
        
        // Create second test post
        Post post2 = new Post();
        post2.setCid("test-cid-2");
        post2.setUri("test-uri-2");
        post2.setContent("This is a test post 2");
        post2.setTimestamp(LocalDateTime.now().minusDays(1));
        post2.setLikeCount(20);
        post2.setRepostCount(15);
        
        Author author2 = new Author();
        author2.setDid("test-author-2");
        author2.setHandle("testuser2");
        post2.setAuthor(author2);
        
        // Add a reply to post2
        List<Post> replies = new ArrayList<>();
        Post reply = new Post();
        reply.setCid("test-reply-1");
        reply.setUri("test-reply-uri-1");
        reply.setContent("This is a reply to post 2");
        reply.setTimestamp(LocalDateTime.now().minusHours(12));
        
        Author replyAuthor = new Author();
        replyAuthor.setDid("test-author-3");
        replyAuthor.setHandle("testuser3");
        reply.setAuthor(replyAuthor);
        
        replies.add(reply);
        post2.setReplies(replies);
        
        posts.add(post1);
        posts.add(post2);
        
        return posts;
    }
} 