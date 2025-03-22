package com.ecs160.hw1;

import com.ecs160.hw1.config.AnalyzerConfig;
import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.utils.parser;
import com.ecs160.hw1.analysis.analyzer;

import java.util.List;

import com.ecs160.hw1.analysis.ComprehensiveAnalyzer;
import com.ecs160.hw1.database.redis;
import com.ecs160.hw1.services.OllamaService;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
            OllamaService testService = new OllamaService();
            AnalyzerConfig config = AnalyzerConfig.getInstance(false, "src/main/resources/input.json");
            // AnalyzerConfig config = AnalyzerConfig.getInstance(false, "src/main/resources/sample_input.json");

            List<Post> posts = parser.parse(config.getJsonFilePath());

            redis redisSession = new redis();
            redisSession.persistAll(posts);

            List<Post> postsFromRedis = redisSession.loadAll();
            analyzer comprehensiveAnalyzer = new ComprehensiveAnalyzer();
            comprehensiveAnalyzer.analyze(postsFromRedis);
        }
    }