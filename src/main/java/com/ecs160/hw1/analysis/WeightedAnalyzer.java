package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;

import java.util.List;

public class weightedAnalyzer implements analyzer {
    @Override
    public void analyze(List<Post> posts) {
        int totalPosts = posts.size();
        double totalReplies = 0;
        int maxPostLength = 0;

        // Find the longest post to calculate weights
        for (Post post : posts) {
            int postLength = post.getRecord().getText().split(" ").length;
            if (postLength > maxPostLength) {
                maxPostLength = postLength;
            }
        }

        System.out.println("Running Weighted Analysis:");

        for (Post post : posts) {
            int postLength = post.getRecord().getText().split(" ").length;
            double postWeight = 1 + ((double) postLength / maxPostLength);
            int actualReplies = post.getReplyCount(); // Actual replies

            double weightedReplies = 0;
            for (int i = 0; i < actualReplies; i++) {
                // Assuming reply weights are similar to the post weight calculation
                int replyLength = post.getRecord().getText().split(" ").length; // Placeholder logic
                double replyWeight = 1 + ((double) replyLength / maxPostLength);
                weightedReplies += replyWeight;
            }

            // Weighted total replies
            totalReplies += weightedReplies;

            System.out.println("Post: " + post.getRecord().getText());
            System.out.println("Weighted Replies: " + weightedReplies);
        }

        // Compute weighted average
        double weightedAvgReplies = totalReplies / totalPosts;
        System.out.println("Total number of posts: " + totalPosts);
        System.out.println("Weighted average number of replies per post: " + weightedAvgReplies);
    }
}
