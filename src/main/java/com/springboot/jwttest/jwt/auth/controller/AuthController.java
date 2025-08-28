package com.springboot.jwttest.jwt.auth.controller;

import com.springboot.jwttest.jwt.auth.dto.LoginReq;
import com.springboot.jwttest.jwt.auth.dto.SignUpReq;
import com.springboot.jwttest.jwt.auth.dto.TokenRes;
import com.springboot.jwttest.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpReq req) {
        authService.signUp(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@Valid @RequestBody LoginReq req) {
        return ResponseEntity.ok(authService.login(req));
    }

    // 프론트: Access 만료시 헤더로 userId/refresh 전달(또는 refresh JWT의 subject 사용)
    //   dddd
    @PostMapping("/refresh")
    public ResponseEntity<TokenRes> refresh(@RequestHeader("X-USER-ID") int userId,
                                            @RequestHeader("X-REFRESH") String refresh) {
        return ResponseEntity.ok(authService.refresh(userId, refresh));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-USER-ID") int userId) {
        authService.logout(userId);
        return ResponseEntity.ok().build();
    }
}
