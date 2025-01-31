package com.ecs160.hw1.analysis;

import com.ecs160.hw1.models.Post;

import java.util.List;

public class basicAnalyzer implements analyzer {
    @Override
    public void analyze(List<Post> posts) {
        int totalPosts = posts.size();
        int totalReplies = 0;

        System.out.println("Running Basic Analysis:");

        for (Post post : posts) {
            // Get actual replies (assuming the reply count is accurate)
            int actualReplies = post.getReplyCount();
            totalReplies += actualReplies;

            System.out.println("Post: " + post.getRecord().getText());
            System.out.println("Likes: " + post.getLikeCount());
            System.out.println("Replies: " + actualReplies);
        }

        // Compute averages
        double avgReplies = (double) totalReplies / totalPosts;
        System.out.println("Total number of posts: " + totalPosts);
        System.out.println("Average number of replies per post: " + avgReplies);
    }
}
