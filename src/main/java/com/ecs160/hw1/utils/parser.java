package com.ecs160.hw1.utils;

import com.ecs160.hw1.models.Post;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class parser {
    public static List<Post> parse(String filePath) {
        List<Post> posts = new ArrayList<>();
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse the entire JSON file
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Get the 'feed' array from the JSON
            JsonArray feed = jsonObject.getAsJsonArray("feed");

            // Iterate through each item in the feed array
            for (int i = 0; i < feed.size(); i++) {
                JsonObject threadItem = feed.get(i).getAsJsonObject();

                // Check if the 'thread' key exists in each item
                if (threadItem.has("thread")) {
                    JsonObject thread = threadItem.getAsJsonObject("thread");

                    // Check if 'post' is available inside 'thread'
                    if (thread.has("post")) {
                        Post post = gson.fromJson(thread.get("post"), Post.class);
                        posts.add(post);
                    } else {
                        System.out.println("'post' is missing inside 'thread' in feed item " + i);
                    }
                } else {
                    System.out.println("'thread' is missing in feed item " + i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
