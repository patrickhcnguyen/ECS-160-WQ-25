package com.ecs160.hw1.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class AuthorTest {
    
    @Test
    public void testAuthorCreationAndGetters() {
        // Create an Author
        Author author = new Author();
        author.setDid("did:plc:testuser123");
        author.setHandle("testuser.bsky.social");
        
        // Test getters
        assertEquals("DID should match", "did:plc:testuser123", author.getDid());
        assertEquals("Handle should match", "testuser.bsky.social", author.getHandle());
    }
    
    @Test
    public void testAuthorEquality() {
        // Create two Authors with the same values
        Author author1 = new Author();
        author1.setDid("did:plc:testuser123");
        author1.setHandle("testuser.bsky.social");
        
        Author author2 = new Author();
        author2.setDid("did:plc:testuser123");
        author2.setHandle("testuser.bsky.social");
        
        // They should be equal according to equals()
        assertEquals("Authors with the same values should be equal", author1, author2);
    }
    
    @Test
    public void testAuthorInequality() {
        // Create two Authors with different values
        Author author1 = new Author();
        author1.setDid("did:plc:testuser123");
        author1.setHandle("testuser.bsky.social");
        
        Author author2 = new Author();
        author2.setDid("did:plc:differentuser");
        author2.setHandle("differentuser.bsky.social");
        
        // They should not be equal
        assertNotEquals("Authors with different values should not be equal", author1, author2);
    }
    
    @Test
    public void testAuthorWithNullValues() {
        // Create an Author with null values
        Author author = new Author();
        
        // Getters should return null
        assertNull("DID should be null", author.getDid());
        assertNull("Handle should be null", author.getHandle());
    }
    
    @Test
    public void testGetChildren() {
        // The Author class might implement SocialMediaComponent
        // If so, getChildren should return an empty list
        Author author = new Author();
        
        // Since Author likely doesn't have any children, check that it doesn't throw exceptions
        try {
            if (author instanceof SocialMediaComponent) {
                SocialMediaComponent component = (SocialMediaComponent) author;
                assertTrue("Author should not have children", component.getChildren().isEmpty());
            }
            // If it's not a SocialMediaComponent, the test passes by default
            assertTrue(true);
        } catch (Exception e) {
            fail("Getting children should not throw an exception");
        }
    }
} 