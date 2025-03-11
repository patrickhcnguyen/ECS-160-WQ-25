package com.ecs160.utils;

import com.ecs160.models.Post;
import com.google.gson.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class parser {
    public static List<Post> parse(InputStream inputStream) {
        List<Post> posts = new ArrayList<>();
        int currentId = 1;

        Reader reader = new InputStreamReader(inputStream);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray feed = jsonObject.getAsJsonArray("feed");

        for (JsonElement feedElement : feed) {
            JsonObject threadItem = feedElement.getAsJsonObject();
            if (threadItem.has("thread")) {
                JsonObject thread = threadItem.getAsJsonObject("thread");
                if (thread.has("post")) {
                    JsonObject postObj = thread.getAsJsonObject("post");
                    Post post = new Post();

                    if (postObj.has("record") &&
                            postObj.getAsJsonObject("record").has("text")) {
                        post.setPostContent(
                                postObj.getAsJsonObject("record")
                                        .get("text")
                                        .getAsString()
                        );
                    }

                    post.setPostId(currentId++);
                    String cid = postObj.has("cid") ? postObj.get("cid").getAsString() : "N/A";
                    post.setCid(cid);

                    // Handle replies
                    if (thread.has("replies")) {
                        JsonArray repliesArray = thread.getAsJsonArray("replies");
                        List<Post> replies = new ArrayList<>();

                        for (JsonElement replyElement : repliesArray) {
                            JsonObject replyObj = replyElement.getAsJsonObject();
                            if (replyObj.has("post")) {
                                JsonObject replyPostObj = replyObj.getAsJsonObject("post");
                                Post reply = new Post();

                                if (replyPostObj.has("record") &&
                                        replyPostObj.getAsJsonObject("record").has("text")) {
                                    reply.setPostContent(
                                            replyPostObj.getAsJsonObject("record")
                                                    .get("text")
                                                    .getAsString()
                                    );
                                }

                                reply.setPostId(currentId++);
                                String replyCid = replyPostObj.has("cid") ? replyPostObj.get("cid").getAsString() : "N/A";
                                reply.setCid(replyCid);
                                replies.add(reply);
                            }
                        }
                        post.setReplies(replies);
                    }

                    posts.add(post);
                }
            }
        }
        return posts;
    }
} 