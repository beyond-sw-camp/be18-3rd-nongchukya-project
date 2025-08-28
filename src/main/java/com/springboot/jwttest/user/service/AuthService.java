package com.springboot.jwttest.user.service;


import com.springboot.jwttest.jwt.auth.dto.LoginReq;
import com.springboot.jwttest.jwt.auth.dto.SignUpReq;
import com.springboot.jwttest.jwt.auth.dto.TokenRes;
import com.springboot.jwttest.jwt.security.JwtTokenProvider;
import com.springboot.jwttest.jwt.security.TokenHashUtil;
import com.springboot.jwttest.jwt.token.mapper.RefreshTokenMapper;
import com.springboot.jwttest.user.mapper.UserMapper;
import com.springboot.jwttest.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtTokenProvider jwt;

    public void signUp(SignUpReq req) {
        User u = new User();
        u.setId(req.id());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setName(req.name());
        u.setNickname(req.nickname());
        u.setStatus("A"); // Active 기본값
//        u.setGender(req.gender());
        u.setGender("M");
//        u.setDmOption(req.dm_option());
        u.setDmOption(true);
        userMapper.insertUser(u);
    }

    public TokenRes login(LoginReq req) {
        User u = userMapper.selectByLogin(req.login());
        if (u == null || !passwordEncoder.matches(req.password(), u.getPassword()))
            throw new BadCredentialsException("아이디/비밀번호가 올바르지 않습니다.");

        String access  = jwt.createAccessToken(u.getUserId(), u.getNickname());
        String refresh = jwt.createRefreshToken(u.getUserId());

        // Refresh 평문 저장 금지 → 해시 보관
        String hash = TokenHashUtil.sha256Base64(refresh);
        refreshTokenMapper.upsert(u.getUserId(), hash, jwt.refreshExpiryLdt());

        return new TokenRes(access, refresh, jwt.accessTtlSeconds());
    }

    public TokenRes refresh(int userId, String refreshToken) {
        String savedHash = refreshTokenMapper.selectHashByUserId(userId);
        if (savedHash == null
                || !jwt.validate(refreshToken) || !jwt.isRefreshToken(refreshToken)
                || !TokenHashUtil.matchesSha256Base64(refreshToken, savedHash)) {
            throw new BadCredentialsException("리프레시 토큰 무효");
        }

        String newAccess  = jwt.createAccessToken(userId, null);
        String newRefresh = jwt.createRefreshToken(userId);
        refreshTokenMapper.upsert(userId,
                TokenHashUtil.sha256Base64(newRefresh),
                jwt.refreshExpiryLdt());

        return new TokenRes(newAccess, newRefresh, jwt.accessTtlSeconds());
    }

    public void logout(int userId) {
        refreshTokenMapper.deleteByUserId(userId);
    }
}
