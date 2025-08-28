package com.springboot.jwttest.jwt.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @NotBlank String login,   // 로그인 ID 또는 이메일
        @NotBlank String password
) {}
