package com.springboot.jwttest.jwt.security;

import com.springboot.jwttest.user.mapper.UserMapper;
import com.springboot.jwttest.user.vo.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;
    private final UserMapper userMapper;
    // 인증,인가
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(req, res);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (jwt.validate(token) && jwt.isAccessToken(token)) {
                Integer uid = jwt.userId(token);
                User u = userMapper.selectByUserId(uid); // 가벼운 조회
                if (u != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            u.getUserId(), null, List.of()); // 필요 시 ROLE 추가
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(req, res);
    }
}
