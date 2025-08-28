package com.springboot.jwttest.post.service;

import com.springboot.jwttest.post.dto.PostRequestDto;
import com.springboot.jwttest.post.mappers.postMapper;
import com.springboot.jwttest.post.vo.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final postMapper postMapper;

    @Override
    public Post create(int currentUserId, PostRequestDto postPostRequestDto) {
        Post post = new Post();
        post.setUserId(currentUserId);
        post.setTitle(postPostRequestDto.getTitle());
        post.setContent(postPostRequestDto.getContent());
        int result = postMapper.insert(post);
        if(result != 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 생성 실패");
        }
        return post;
    }
}
