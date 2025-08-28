package com.springboot.jwttest.jwt.auth.dto;

import com.springboot.jwttest.user.vo.User;

public record MeRes(
        Integer userId, String id, String email,
        String nickname, String profileImage, String status
) {
    public static MeRes from(User u) {
        return new MeRes(u.getUserId(), u.getId(), u.getEmail(),
                u.getNickname(), u.getProfileImage(), u.getStatus());
    }
}
