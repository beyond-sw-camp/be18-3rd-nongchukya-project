package com.springboot.jwttest.user.mapper;

import com.springboot.jwttest.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectByLogin(@Param("login") String login);

    // access 토큰 인증 시: PK로 조회
    User selectByUserId(@Param("userId") Integer userId);

    int insertUser(User user);
}
