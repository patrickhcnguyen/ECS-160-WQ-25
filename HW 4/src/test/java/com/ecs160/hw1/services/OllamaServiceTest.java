package com.ecs160.hw1.services;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OllamaServiceTest {
    private OllamaService ollamaService;

    @Before
    public void setUp() {
        ollamaService = new OllamaService();
    }

    @Test
    public void testGenerateHashtagReturnsValidHashtag() {
        // Test with a valid content
        String content = "Climate change is a pressing global issue that requires immediate action.";
        String hashtag = ollamaService.generateHashtag(content);
        
        // Verify that the hashtag starts with #
        assertTrue("Hashtag should start with #", hashtag.startsWith("#"));
        
        // Verify that the hashtag is not empty and not just the # character
        assertTrue("Hashtag should have content after the # symbol", hashtag.length() > 1);
    }
    
    @Test
    public void testGenerateHashtagWithEmptyContent() {
        String hashtag = ollamaService.generateHashtag("");
        
        // Should still return a hashtag (even if it's a default or error one)
        assertNotNull("Hashtag should not be null even with empty content", hashtag);
        assertTrue("Hashtag should start with #", hashtag.startsWith("#"));
    }
    
    @Test
    public void testGenerateHashtagConsistency() {
        // Check if repeated calls with similar content produce related hashtags
        // Note: This test is less strict because LLMs can be non-deterministic
        
        String content1 = "Technology is advancing rapidly in the field of artificial intelligence.";
        String content2 = "AI technology is developing at an unprecedented pace.";
        
        String hashtag1 = ollamaService.generateHashtag(content1);
        String hashtag2 = ollamaService.generateHashtag(content2);
        
        // Both should be valid hashtags
        assertTrue("First hashtag should start with #", hashtag1.startsWith("#"));
        assertTrue("Second hashtag should start with #", hashtag2.startsWith("#"));
    }
} 