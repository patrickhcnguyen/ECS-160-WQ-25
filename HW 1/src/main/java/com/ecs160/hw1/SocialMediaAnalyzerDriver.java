package com.ecs160.hw1;

import java.util.List;

import com.ecs160.hw1.analysis.analyzer;
import com.ecs160.hw1.database.redis;
import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.utils.parser;
import com.ecs160.hw1.analysis.ComprehensiveAnalyzer;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        String filePath = "/Users/patricknguyen/Desktop/ECS-160-WQ-25/HW 1/src/main/resources/input.json";
        redis redisSession = null;
            List<Post> posts = parser.parse(filePath);

            redisSession = new redis();
            redisSession.persistAll(posts);

            List<Post> postsReloaded = redisSession.loadAll();

            analyzer comprehensiveAnalyzer = new ComprehensiveAnalyzer();
            comprehensiveAnalyzer.analyze(postsReloaded);
    }
}