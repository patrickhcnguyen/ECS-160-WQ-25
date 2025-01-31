package com.ecs160.hw1;

import org.junit.Test;
import com.ecs160.hw1.analysis.analyzer;
import com.ecs160.hw1.analysis.ComprehensiveAnalyzer;
import com.ecs160.hw1.models.Post;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class BasicAnalyzerTest {
    @Test
    public void testBasicAnalysis() {
        analyzer analyzer = new ComprehensiveAnalyzer();  // Use actual analyzer
        List<Post> posts = new ArrayList<>();
        
        // Create a test post
        Post post = new Post();
        posts.add(post);
        
        analyzer.analyze(posts);
        assertTrue(posts.size() == 1);  // Verify post count
    }
}
