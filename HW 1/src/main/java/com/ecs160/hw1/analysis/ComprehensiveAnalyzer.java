package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ComprehensiveAnalyzer implements analyzer {
    @Override
    public void analyze(List<Post> posts) {

        int totalPosts = posts.size();
        int totalReplies = 0;
        long totalIntervalSeconds = 0;
        int totalIntervalPairs = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        for (Post post : posts) {
            int replies = post.getReplyCount();
            totalReplies += replies;

            List<Post> postReplies = post.getReplies();
            if (postReplies != null && postReplies.size() > 1) {
                for (int i = 0; i < postReplies.size() - 1; i++) {
                    LocalDateTime time1 = parseTimestamp(postReplies.get(i).getRecord().getCreatedAt(), formatter);
                    LocalDateTime time2 = parseTimestamp(postReplies.get(i + 1).getRecord().getCreatedAt(), formatter);

                    if (time1 != null && time2 != null) {
                        Duration duration = Duration.between(time1, time2);
                        totalIntervalSeconds += duration.getSeconds();
                        totalIntervalPairs++;
                    }
                }
            }
        }

        // calc avgs
        double avgReplies = totalReplies / (double) totalPosts;
        long avgIntervalSeconds = totalIntervalPairs > 0 ? totalIntervalSeconds / totalIntervalPairs : 0;

        long hours = avgIntervalSeconds / 3600;
        long minutes = (avgIntervalSeconds % 3600) / 60;
        long seconds = avgIntervalSeconds % 60;
        String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        System.out.println("Total posts: " + totalPosts);
        System.out.println("Average number of replies: " + avgReplies);
        System.out.println("Average duration between replies: " + formattedDuration);
    }

    private LocalDateTime parseTimestamp(String timestamp, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(timestamp, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}