package com.ecs160.hw1.utils;

import com.ecs160.hw1.models.Post;
import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class parser {
    public static List<Post> parse(String filePath) {
        List<Post> posts = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray feed = jsonObject.getAsJsonArray("feed");

            for (int i = 0; i < feed.size(); i++) {
                JsonObject threadItem = feed.get(i).getAsJsonObject();
                if (threadItem.has("thread")) {
                    JsonObject thread = threadItem.getAsJsonObject("thread");
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