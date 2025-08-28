package com.springboot.jwttest.post.mappers;

import com.springboot.jwttest.post.vo.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface postMapper {
    int insert(Post post);
}
