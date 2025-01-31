package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class ComprehensiveAnalyzer implements analyzer {
    private boolean weighted = false;

    @Override
    public void analyze(List<Post> posts) {
        int totalPosts = posts.size();
        int totalPostsAndReplies = totalPosts; // Start with initial posts
        int totalReplies = 0;
        long totalIntervalSeconds = 0;
        int totalIntervalPairs = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        for (Post post : posts) {
            List<Post> replies = post.getReplies();
            if (replies != null && !replies.isEmpty()) {
                totalReplies += replies.size();
                totalPostsAndReplies += replies.size();

                replies.sort(Comparator.comparing(p -> parseTimestamp(p.getRecord().getCreatedAt(), formatter)));

                if (replies.size() > 1) {
                    for (int i = 0; i < replies.size() - 1; i++) {
                        LocalDateTime time1 = parseTimestamp(replies.get(i).getRecord().getCreatedAt(), formatter);
                        LocalDateTime time2 = parseTimestamp(replies.get(i + 1).getRecord().getCreatedAt(), formatter);

                        if (time1 != null && time2 != null) {
                            Duration duration = Duration.between(time1, time2);
                            // Only add positive durations to avoid negative intervals
                            if (!duration.isNegative()) {
                                totalIntervalSeconds += duration.getSeconds();
                                totalIntervalPairs++;
                            }
                        }
                    }
                }
            }
        }

        double avgReplies = (double) totalReplies / totalPosts;
        long avgIntervalSeconds = totalIntervalPairs > 0 ? totalIntervalSeconds / totalIntervalPairs : 0;

        System.out.println("Total posts: " + totalPosts);
        System.out.println("Total replies: " + totalReplies);
        System.out.println("Total posts and replies: " + totalPostsAndReplies);
        System.out.println("Average number of replies per post: " + String.format("%.2f", avgReplies));
        System.out.println("Average duration between replies: " + formatDuration(avgIntervalSeconds));
    }

    private LocalDateTime parseTimestamp(String timestamp, DateTimeFormatter formatter) {
        return LocalDateTime.parse(timestamp, formatter);
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
