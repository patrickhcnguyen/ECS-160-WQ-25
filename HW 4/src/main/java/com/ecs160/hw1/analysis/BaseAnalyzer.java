package com.ecs160.hw1.analysis;

import com.ecs160.hw1.analysis.visitors.StatisticsVisitor;
import com.ecs160.hw1.config.AnalyzerConfig;
import com.ecs160.hw1.models.Post;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAnalyzer implements analyzer {
    protected final AnalyzerConfig config;
    protected final List<StatisticsVisitor> visitors = new ArrayList<>();

    protected BaseAnalyzer() {
        this.config = AnalyzerConfig.getInstance(false, "src/main/resources/input.json");
    }

    @Override
    public void analyze(List<Post> posts) {
        initializeVisitors();
        
        for (Post post : posts) {
            for (StatisticsVisitor visitor : visitors) {
                visitor.visit(post);
                if (post.getReplies() != null) {
                    for (Post reply : post.getReplies()) {
                        visitor.visit(reply);
                    }
                }
            }
        }

        for (StatisticsVisitor visitor : visitors) {
            visitor.getResults();
        }
    }

    protected abstract void initializeVisitors();
} 