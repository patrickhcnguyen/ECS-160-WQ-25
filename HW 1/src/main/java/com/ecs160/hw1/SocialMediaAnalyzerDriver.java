package com.ecs160.hw1;

import java.util.List;

import com.ecs160.hw1.analysis.analyzer;
import com.ecs160.hw1.database.redis;
import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.utils.parser;
import com.ecs160.hw1.analysis.ComprehensiveAnalyzer;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        String filePath = "src/main/resources/input.json";
        redis redisSession = null;

        try {
            List<Post> posts = parser.parse(filePath);

            redisSession = new redis();
            redisSession.persistAll(posts);

            List<Post> postsReloaded = redisSession.loadAll();

            analyzer comprehensiveAnalyzer = new ComprehensiveAnalyzer();
            comprehensiveAnalyzer.analyze(postsReloaded);

        } catch (Exception e) {
            System.err.println("Fatal error in main: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (redisSession != null) {
                try {
                    redisSession.close();
                } catch (Exception e) {
                    System.err.println("Error closing Redis: " + e.getMessage());
                }
            }
        }
    }
}