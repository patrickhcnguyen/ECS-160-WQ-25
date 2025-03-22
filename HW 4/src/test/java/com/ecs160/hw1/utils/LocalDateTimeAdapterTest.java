package com.ecs160.hw1.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapterTest {
    private LocalDateTimeAdapter adapter;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
    @Before
    public void setUp() {
        adapter = new LocalDateTimeAdapter();
    }
    
    @Test
    public void testSerialize() {
        // Create a specific LocalDateTime for testing
        LocalDateTime dateTime = LocalDateTime.of(2023, 6, 1, 12, 0, 0);
        
        // Serialize it using the adapter
        JsonElement jsonElement = adapter.serialize(dateTime, null, null);
        
        // Verify that it's serialized correctly as ISO format
        assertTrue("Should be a primitive JSON element", jsonElement.isJsonPrimitive());
        assertEquals("Should serialize to ISO format", 
                "2023-06-01T12:00:00", 
                jsonElement.getAsString());
    }
    
    @Test
    public void testDeserialize() {
        // Create a JSON primitive with an ISO date string
        JsonPrimitive jsonPrimitive = new JsonPrimitive("2023-06-01T12:00:00");
        
        // Deserialize it using the adapter
        LocalDateTime dateTime = adapter.deserialize(jsonPrimitive, null, null);
        
        // Verify that it's deserialized correctly
        assertNotNull("Deserialized LocalDateTime should not be null", dateTime);
        assertEquals("Year should be 2023", 2023, dateTime.getYear());
        assertEquals("Month should be 6", 6, dateTime.getMonthValue());
        assertEquals("Day should be 1", 1, dateTime.getDayOfMonth());
        assertEquals("Hour should be 12", 12, dateTime.getHour());
        assertEquals("Minute should be 0", 0, dateTime.getMinute());
        assertEquals("Second should be 0", 0, dateTime.getSecond());
    }
    
    @Test
    public void testRoundTrip() {
        // Create a LocalDateTime
        LocalDateTime original = LocalDateTime.now();
        
        // Serialize it
        JsonElement jsonElement = adapter.serialize(original, null, null);
        
        // Deserialize it back
        LocalDateTime roundTripped = adapter.deserialize(jsonElement, null, null);
        
        // Verify that they match
        assertEquals("Round-tripped LocalDateTime should match original", 
                original.format(formatter), 
                roundTripped.format(formatter));
    }
} 