package com.springboot.jwttest.post.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private int postId;
    private int userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
