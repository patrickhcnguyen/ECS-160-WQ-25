package com.ecs160.hw1;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import com.ecs160.hw1.analysis.TimeAnalyzer;
import com.ecs160.hw1.analysis.analyzer;
import com.ecs160.hw1.analysis.basicAnalyzer;
import com.ecs160.hw1.analysis.weightedAnalyzer;
import com.ecs160.hw1.database.redis;
import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.utils.parser;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        String filePath = "src/main/resources/input.json";

        List<Post> posts = parser.parse(filePath);

        redis redisSession = new redis();
        redisSession.persistAll(posts);

        List<Post> postsReloaded = redisSession.loadAll();

        analyzer basicAnalyzer = new basicAnalyzer();
        basicAnalyzer.analyze(postsReloaded);

        analyzer weightedAnalyzer = new weightedAnalyzer();
        weightedAnalyzer.analyze(postsReloaded);

        TimeAnalyzer timeAnalyzer = new TimeAnalyzer();
        timeAnalyzer.analyze(postsReloaded);
    }
}
