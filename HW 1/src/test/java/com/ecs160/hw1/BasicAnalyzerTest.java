package com.ecs160.hw1;

import org.junit.Test;
import com.ecs160.hw1.analysis.analyzer;
import com.ecs160.hw1.analysis.ComprehensiveAnalyzer;
import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.models.Record;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class BasicAnalyzerTest {
    @Test
    public void testEmptyPosts() {
        analyzer analyzer = new ComprehensiveAnalyzer();
        List<Post> posts = new ArrayList<>();
        analyzer.analyze(posts);
        assertTrue(posts.isEmpty());
    }

    @Test
    public void testSinglePostNoReplies() {
        analyzer analyzer = new ComprehensiveAnalyzer();
        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        posts.add(post);
        analyzer.analyze(posts);
        assertEquals(1, posts.size());
        assertEquals(0, post.getReplyCount());
    }

    @Test
    public void testPostWithReplies() {
        analyzer analyzer = new ComprehensiveAnalyzer();
        List<Post> posts = new ArrayList<>();
        Post mainPost = new Post();
        List<Post> replies = new ArrayList<>();
        
        // Add two replies with timestamps
        Post reply1 = new Post();
        Record record1 = new Record();
        record1.setCreatedAt("2024-01-31T10:00:00Z");
        reply1.setRecord(record1);
        
        Post reply2 = new Post();
        Record record2 = new Record();
        record2.setCreatedAt("2024-01-31T10:30:00Z");
        reply2.setRecord(record2);
        
        replies.add(reply1);
        replies.add(reply2);
        mainPost.setReplies(replies);
        posts.add(mainPost);
        
        analyzer.analyze(posts);
        assertEquals(2, mainPost.getReplies().size());
    }

    @Test
    public void testMultiplePostsWithReplies() {
        analyzer analyzer = new ComprehensiveAnalyzer();
        List<Post> posts = new ArrayList<>();
        
        // First post with 2 replies
        Post post1 = new Post();
        List<Post> replies1 = new ArrayList<>();
        
        // Add timestamps to replies
        Post reply1 = new Post();
        Record record1 = new Record();
        record1.setCreatedAt("2024-01-31T10:00:00Z");
        reply1.setRecord(record1);
        
        Post reply2 = new Post();
        Record record2 = new Record();
        record2.setCreatedAt("2024-01-31T10:30:00Z");
        reply2.setRecord(record2);
        
        replies1.add(reply1);
        replies1.add(reply2);
        post1.setReplies(replies1);
        
        // Second post with 1 reply
        Post post2 = new Post();
        List<Post> replies2 = new ArrayList<>();
        Post reply3 = new Post();
        Record record3 = new Record();
        record3.setCreatedAt("2024-01-31T11:00:00Z");
        reply3.setRecord(record3);
        replies2.add(reply3);
        post2.setReplies(replies2);
        
        posts.add(post1);
        posts.add(post2);
        
        analyzer.analyze(posts);
        assertEquals(2, posts.size());
        assertEquals(2, post1.getReplies().size());
        assertEquals(1, post2.getReplies().size());
    }

    @Test
    public void testPostWithTimestampedReplies() {
        analyzer analyzer = new ComprehensiveAnalyzer();
        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        List<Post> replies = new ArrayList<>();
        
        // Create three replies with specific timestamps
        String[] timestamps = {
            "2024-01-31T10:00:00Z",
            "2024-01-31T10:15:00Z",
            "2024-01-31T10:45:00Z"
        };
        
        for (String timestamp : timestamps) {
            Post reply = new Post();
            Record record = new Record();
            record.setCreatedAt(timestamp);
            reply.setRecord(record);
            replies.add(reply);
        }
        
        post.setReplies(replies);
        posts.add(post);
        
        analyzer.analyze(posts);
        assertEquals(3, post.getReplies().size());
    }
}
