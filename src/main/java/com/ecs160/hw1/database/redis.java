package com.ecs160.hw1.database;

import com.ecs160.hw1.models.Post;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class redis {
    private final Jedis jedis;
    private final Gson gson;

    public redis() {
        this.jedis = new Jedis("localhost", 6379); // Connect to Redis
        this.gson = new Gson();
    }

    public void persistAll(List<Post> posts) {
        for (int i = 0; i < posts.size(); i++) {
            jedis.set("post:" + i, gson.toJson(posts.get(i)));
        }
    }

    public List<Post> loadAll() {
        List<Post> posts = new ArrayList<>();
        for (String key : jedis.keys("post:*")) {
            posts.add(gson.fromJson(jedis.get(key), Post.class));
        }
        return posts;
    }
}
