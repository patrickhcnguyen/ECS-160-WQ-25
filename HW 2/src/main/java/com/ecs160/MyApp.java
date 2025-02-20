package com.ecs160;

import com.ecs160.models.Post;
import com.ecs160.persistence.Session;
import com.ecs160.utils.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class MyApp {
    public static void main(String[] args) throws Exception {
            InputStream inputStream = MyApp.class.getClassLoader().getResourceAsStream("input.json");
            List<Post> posts = parser.parse(inputStream);

            Session session = new Session("localhost", 6379);

            for (Post post : posts) {
                session.add(post);
                if (post.getReplies() != null) {
                    for (Post reply : post.getReplies()) {
                        session.add(reply);
                        if (reply.getReplies() != null) {
                            for (Post nestedReply : reply.getReplies()) {
                                session.add(nestedReply);
                            }
                        }
                    }
                }
            }

            session.persistAll();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter post ID: ");
            int postId = scanner.nextInt();

            Post template = new Post();
            template.setPostId(postId);
            Post loadedPost = (Post) session.load(template);

            if (loadedPost != null) {
                System.out.println("> " + loadedPost.getPostContent());

                if (loadedPost.getReplies() != null) {
                    for (Post reply : loadedPost.getReplies()) {
                        System.out.println("--> " + reply.getPostContent());
                    }
                }
            }
            scanner.close();
            session.close();
        }
    }