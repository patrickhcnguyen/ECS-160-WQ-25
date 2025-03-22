package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;
import com.ecs160.hw1.analysis.visitors.PostCountVisitor;
import com.ecs160.hw1.analysis.visitors.HashtagVisitor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class ComprehensiveAnalyzer extends BaseAnalyzer {

    @Override
    public void analyze(List<Post> posts) {
        super.analyze(posts);

        analyzeStatistics(posts);
    }

    private void analyzeStatistics(List<Post> posts) {
        int totalPosts = posts.size();
        int totalReplies = 0;
        long totalIntervalSeconds = 0;
        int totalIntervalPairs = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        for (Post post : posts) {
            List<Post> replies = post.getReplies();
            if (replies != null && !replies.isEmpty()) {
                totalReplies += replies.size();

                replies.sort(Comparator.comparing(p -> parseTimestamp(p.getRecord().getCreatedAt(), formatter)));

                if (replies.size() > 1) {
                    for (int i = 0; i < replies.size() - 1; i++) {
                        LocalDateTime time1 = parseTimestamp(replies.get(i).getRecord().getCreatedAt(), formatter);
                        LocalDateTime time2 = parseTimestamp(replies.get(i + 1).getRecord().getCreatedAt(), formatter);

                        Duration duration = Duration.between(time1, time2);
                        if (!duration.isNegative()) {
                            totalIntervalSeconds += duration.getSeconds();
                            totalIntervalPairs++;
                        }
                    }
                }
            }
        }

        double avgReplies = (double) totalReplies / totalPosts;
        long avgIntervalSeconds = totalIntervalPairs > 0 ? totalIntervalSeconds / totalIntervalPairs : 0;

        System.out.println("Total posts: " + totalPosts);
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
    
    @Override
    protected void initializeVisitors() {
        visitors.add(new PostCountVisitor(config.isWeighted()));
        visitors.add(new HashtagVisitor());
    }
}
