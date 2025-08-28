package com.springboot.jwttest.jwt.token.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface RefreshTokenMapper {

    int upsert(@Param("userId") int userId,
               @Param("tokenHash") String tokenHash,
               @Param("expiresAt") LocalDateTime expiresAt);

    String selectHashByUserId(@Param("userId") int userId);

    int deleteByUserId(@Param("userId") int userId);
}
