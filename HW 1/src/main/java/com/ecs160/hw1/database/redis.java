package com.ecs160.hw1.database;

import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.utils.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class redis {
    private Jedis jedis;
    private final Gson gson;
    private static final String REDIS_PREFIX = "post:";
    private static final int MAX_RETRY = 3;
    private static final int RETRY_DELAY_MS = 1000;

    public redis() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        connectWithRetry();
    }

    private void connectWithRetry() {
        for (int i = 0; i < MAX_RETRY; i++) {
            try {
                this.jedis = new Jedis("localhost", 6379);
                return;
            } catch (JedisConnectionException e) {
                System.err.println("Attempt " + (i + 1) + " to connect to Redis failed");
                if (i < MAX_RETRY - 1) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        throw new JedisConnectionException("Failed to connect to Redis after " + MAX_RETRY + " attempts");
    }

    public void persistAll(List<Post> posts) {
        try {

            jedis.flushAll();

            Pipeline pipeline = jedis.pipelined();
            for (int i = 0; i < posts.size(); i++) {
                String key = REDIS_PREFIX + i;
                String value = gson.toJson(posts.get(i));
                pipeline.set(key, value);
            }
            pipeline.sync();

            // Verify persistence
            long dbSize = jedis.dbSize();
            if (dbSize != posts.size()) {
                System.err.println("Warning: Expected " + posts.size() + " records but found " + dbSize);
            }

        } catch (JedisConnectionException e) {
            connectWithRetry();
            persistAll(posts); // Retry the operation
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist posts to Redis", e);
        }
    }

    public List<Post> loadAll() {
        List<Post> posts = new ArrayList<>();
        try {

            Set<String> keys = jedis.keys(REDIS_PREFIX + "*");
            for (String key : keys) {
                String json = jedis.get(key);
                if (json != null) {
                    Post post = gson.fromJson(json, Post.class);
                    posts.add(post);
                }
            }

        } catch (JedisConnectionException e) {
            connectWithRetry();
            return loadAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load posts from Redis", e);
        }
        return posts;
    }

    public void close() {
        if (jedis != null && jedis.isConnected()) {
            try {
                jedis.close();
            } catch (Exception e) {
                System.err.println("Error closing Redis connection: " + e.getMessage());
            }
        }
    }
}