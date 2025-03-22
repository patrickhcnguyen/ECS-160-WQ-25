package com.ecs160.hw1.analysis.visitors;

import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.decorators.HashtagDecorator;
import com.ecs160.hw1.services.OllamaService;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;

public class HashtagVisitor implements StatisticsVisitor {
    private final PriorityQueue<Post> topPosts;
    private final OllamaService ollamaService;
    
    public HashtagVisitor() {
        this.topPosts = new PriorityQueue<>((p1, p2) -> p2.getLikeCount() - p1.getLikeCount());
        this.ollamaService = new OllamaService();
    }

    @Override
    public void visit(Post post) {
        System.out.println("Visiting post with like count: " + post.getLikeCount() + 
                         ", content: " + (post.getRecord() != null ? post.getRecord().getText() : "null"));
        
        topPosts.offer(post);
        if (topPosts.size() > 10) {
            topPosts.poll(); 
        }
    }

    @Override
    public void getResults() {
        List<Post> top10Posts = new ArrayList<>();
        System.out.println("Number of posts collected: " + topPosts.size());
        
        while (!topPosts.isEmpty()) {
            Post post = topPosts.poll();
            if (post.getRecord() != null && post.getRecord().getText() != null) {
                top10Posts.add(0, post);    
            }
        }

        System.out.println("\nGenerating hashtags for top " + top10Posts.size() + " most-liked posts...");

        ExecutorService executor = Executors.newFixedThreadPool(3); 
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Post post : top10Posts) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    HashtagDecorator decorator = new HashtagDecorator(post);
                    String content = post.getRecord().getText();
                    System.out.println("\nProcessing post with likes: " + post.getLikeCount());
                    System.out.println("Content: " + content);
                    
                    String hashtag = ollamaService.generateHashtag(content);
                    System.out.println("Generated " + hashtag + " for post: " + content.substring(0, Math.min(50, content.length())) + "...");
                    decorator.setHashtag(hashtag);
                } catch (Exception e) {
                    System.err.println("Error processing post: " + e.getMessage());
                }
            }, executor);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        System.out.println("\nAll Post Hashtags:");
        HashtagDecorator.printHashtags();
    }
} 