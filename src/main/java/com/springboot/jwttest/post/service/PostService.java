package com.springboot.jwttest.post.service;

import com.springboot.jwttest.post.dto.PostRequestDto;
import com.springboot.jwttest.post.vo.Post;

public interface PostService {
    Post create(int currentUserId, PostRequestDto postPostRequestDto);
}
