package com.springboot.jwttest.user.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Integer userId;        // PK
    private String id;             // 로그인 ID
    private String email;
    private String password;       // BCrypt(60)
    private String profileImage;
    private String name;
    private String nickname;
    private String gender;         // ENUM in DB (M/F 등)
    private Integer age;
    private String address;
    private String phoneNumber;
    private Boolean dmOption;
    private String status;         // CHAR (e.g., A/I)
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
