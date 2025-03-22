package com.ecs160.hw1.analysis.visitors;

import com.ecs160.hw1.models.Post;

public class PostCountVisitor implements StatisticsVisitor {
    private int totalPosts = 0;
    private final boolean isWeighted;
    private int maxWordCount = 0;

    public PostCountVisitor(boolean isWeighted) {
        this.isWeighted = isWeighted;
    }

    @Override
    public void visit(Post post) {
        if (isWeighted) {
            String content = post.getContent();
            int wordCount = content.split("\\s+").length;
            maxWordCount = Math.max(maxWordCount, wordCount);
            
            double weight = calculateWeight(post);
            totalPosts += (int) weight;
        } else {
            totalPosts++;
        }
    }

    private double calculateWeight(Post post) {
        String content = post.getContent();
        int wordCount = content.split("\\s+").length;
        return maxWordCount > 0 ? 1 + ((double) wordCount / maxWordCount) : 1;
    }

    @Override
    public void getResults() {
        System.out.println("Total posts: " + totalPosts);
    }
}