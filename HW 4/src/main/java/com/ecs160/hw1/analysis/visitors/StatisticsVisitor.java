package com.ecs160.hw1.analysis.visitors;

import com.ecs160.hw1.models.Post;

public interface StatisticsVisitor {
    void visit(Post post);
    void getResults();
}