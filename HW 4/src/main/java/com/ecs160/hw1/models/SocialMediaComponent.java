package com.ecs160.hw1.models;

import java.time.LocalDateTime;
import java.util.List;

public interface SocialMediaComponent {
    String getContent();
    int getLikeCount();
    LocalDateTime getTimestamp();
    List<SocialMediaComponent> getChildren();
}
