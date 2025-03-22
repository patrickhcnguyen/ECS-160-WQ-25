package com.ecs160.hw1.utils;

import com.ecs160.hw1.models.Post;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ParserTest {
    private static final String TEST_JSON_FILE = "src/test/resources/test_input.json";
    
    @Before
    public void setUp() {
        // Create test directory if it doesn't exist
        File resourcesDir = new File("src/test/resources");
        if (!resourcesDir.exists()) {
            resourcesDir.mkdirs();
        }
        
        // Create a test JSON file
        try (FileWriter writer = new FileWriter(TEST_JSON_FILE)) {
            writer.write("{\n" +
                    "  \"feed\": [\n" +
                    "    {\n" +
                    "      \"thread\": {\n" +
                    "        \"post\": {\n" +
                    "          \"uri\": \"at://did:plc:test1/app.bsky.feed.post/test-1\",\n" +
                    "          \"cid\": \"test-cid-1\",\n" +
                    "          \"author\": {\n" +
                    "            \"did\": \"did:plc:test1\",\n" +
                    "            \"handle\": \"test1.bsky.social\"\n" +
                    "          },\n" +
                    "          \"record\": {\n" +
                    "            \"text\": \"This is a test post\",\n" +
                    "            \"createdAt\": \"2023-06-01T12:00:00Z\"\n" +
                    "          },\n" +
                    "          \"replyCount\": 1,\n" +
                    "          \"repostCount\": 2,\n" +
                    "          \"likeCount\": 3,\n" +
                    "          \"indexedAt\": \"2023-06-01T12:01:00Z\"\n" +
                    "        },\n" +
                    "        \"replies\": [\n" +
                    "          {\n" +
                    "            \"post\": {\n" +
                    "              \"uri\": \"at://did:plc:test2/app.bsky.feed.post/test-2\",\n" +
                    "              \"cid\": \"test-cid-2\",\n" +
                    "              \"author\": {\n" +
                    "                \"did\": \"did:plc:test2\",\n" +
                    "                \"handle\": \"test2.bsky.social\"\n" +
                    "              },\n" +
                    "              \"record\": {\n" +
                    "                \"text\": \"This is a reply\",\n" +
                    "                \"createdAt\": \"2023-06-01T12:05:00Z\"\n" +
                    "              },\n" +
                    "              \"replyCount\": 0,\n" +
                    "              \"repostCount\": 0,\n" +
                    "              \"likeCount\": 1,\n" +
                    "              \"indexedAt\": \"2023-06-01T12:06:00Z\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseValidJsonFile() {
        List<Post> posts = parser.parse(TEST_JSON_FILE);
        
        // Check that we got the expected number of posts
        assertEquals("Should parse 1 main post", 1, posts.size());
        
        // Check the content of the main post
        Post mainPost = posts.get(0);
        assertEquals("Main post URI should match", "at://did:plc:test1/app.bsky.feed.post/test-1", mainPost.getUri());
        assertEquals("Main post CID should match", "test-cid-1", mainPost.getCid());
        assertEquals("Main post author handle should match", "test1.bsky.social", mainPost.getAuthor().getHandle());
        
        // Check that it has replies
        assertNotNull("Main post should have replies", mainPost.getReplies());
        assertEquals("Main post should have 1 reply", 1, mainPost.getReplies().size());
        
        // Check the content of the reply
        Post reply = mainPost.getReplies().get(0);
        assertEquals("Reply URI should match", "at://did:plc:test2/app.bsky.feed.post/test-2", reply.getUri());
        assertEquals("Reply CID should match", "test-cid-2", reply.getCid());
        assertEquals("Reply author handle should match", "test2.bsky.social", reply.getAuthor().getHandle());
    }
    
    @Test
    public void testParseNonExistentFile() {
        List<Post> posts = parser.parse("nonexistent_file.json");
        
        // Should return an empty list, not throw an exception
        assertNotNull("Should return an empty list for non-existent file", posts);
        assertTrue("List should be empty for non-existent file", posts.isEmpty());
    }
} 