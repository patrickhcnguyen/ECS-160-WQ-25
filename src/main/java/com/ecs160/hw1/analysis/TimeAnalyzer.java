package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimeAnalyzer implements analyzer {

    @Override
    public void analyze(List<Post> posts) {
        long totalIntervalSeconds = 0;
        int totalCommentPairs = 0;

        // Update the formatter to handle the full ISO 8601 format including 'T' and 'Z'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Output file path
        String outputFilePath = "time_analysis_output.txt";

        // Create a BufferedWriter to write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Writing the header to the file
            writer.write("Running Time Interval Analysis:\n");

            for (Post post : posts) {
                // Get the post creation time
                LocalDateTime postTime = LocalDateTime.parse(post.getRecord().getCreatedAt(), formatter);

                // Write post time to the file
                writer.write("Post created at: " + postTime.toString() + "\n");

                // Simulate reply intervals based on the number of replies
                List<Post> replies = post.getReplies(); // Assuming replies are a list of Post objects
                if (replies != null && replies.size() > 1) {
                    for (int i = 0; i < replies.size() - 1; i++) {
                        // Get the creation time of two consecutive replies
                        LocalDateTime commentTime1 = LocalDateTime.parse(replies.get(i).getRecord().getCreatedAt(), formatter);
                        LocalDateTime commentTime2 = LocalDateTime.parse(replies.get(i + 1).getRecord().getCreatedAt(), formatter);

                        // Calculate the duration between the two replies
                        Duration duration = Duration.between(commentTime1, commentTime2);
                        totalIntervalSeconds += duration.getSeconds();
                        totalCommentPairs++;

                        // Write the time interval to the file
                        writer.write("Time interval between replies: " +
                                duration.getSeconds() + " seconds\n");
                    }
                }
            }

            // Calculate the average interval
            if (totalCommentPairs > 0) {
                long avgIntervalSeconds = totalIntervalSeconds / totalCommentPairs;
                long hours = avgIntervalSeconds / 3600;
                long minutes = (avgIntervalSeconds % 3600) / 60;
                long seconds = avgIntervalSeconds % 60;

                // Write the average interval to the file
                writer.write("\nAverage time interval between comments: " + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "\n");
            } else {
                writer.write("\nNo comment pairs found.\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
