package com.springboot.jwttest.jwt.auth.dto;

public record TokenRes(
        String accessToken,
        String refreshToken,
        long   expiresInSec
) {}
