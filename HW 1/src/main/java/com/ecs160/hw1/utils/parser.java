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

                        if (thread.has("replies")) {
                            JsonArray repliesArray = thread.getAsJsonArray("replies");
                            List<Post> replies = new ArrayList<>();
                            for (int j = 0; j < repliesArray.size(); j++) {
                                JsonObject replyItem = repliesArray.get(j).getAsJsonObject();
                                if (replyItem.has("post")) {
                                    Post reply = gson.fromJson(replyItem.get("post"), Post.class);
                                    replies.add(reply);
                                }
                            }
                            post.setReplies(replies); // set replies to the post
                        }

                        posts.add(post);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
