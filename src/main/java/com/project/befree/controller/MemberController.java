package com.project.befree.controller;

import com.project.befree.domain.Member;
import com.project.befree.dto.MemberDTO;
import com.project.befree.dto.MemberFormDTO;
import com.project.befree.service.MemberService;
import com.project.befree.util.CustomJWTException;
import com.project.befree.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody MemberFormDTO memberFormDTO) {
        log.info("******************** MemberController - register :{}", memberFormDTO);
        Member check = memberService.getOne(memberFormDTO.getEmail());
        if (check != null) {
            return Map.of("error", "already exists");
        }
        MemberDTO memberDTO =
                new MemberDTO(memberFormDTO.getEmail(),
                        passwordEncoder.encode(memberFormDTO.getPassword()),
                        memberFormDTO.getName(),
                        true, false);
        Map<String, String> result = memberService.save(memberDTO);
        return result;
    }

    @GetMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
        log.info("******************** MemberController - authHeader:{}", authHeader);
        log.info("******************** MemberController - refreshToken:{}", refreshToken);
        // refreshToken 없는 경우
        if (refreshToken == null) {
            throw new CustomJWTException(("NULL_REFRESH_TOKEN"));
        }
        // authorization 값이 이상한 경우
        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_HEADER");
        }
        // accessToken 만료되지 않은 경우
        String accessToken = authHeader.substring(7);
        if (checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }
        // accessToken 만료된 경우
        // refreshToken 검증하고, claims 리턴받아 새 토큰 생성시 사용
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        // 새 토큰 생성해서 전달
        String newAccessToken = jwtUtil.generateToken(claims, 10);
        String newRefreshToken = checkRemainTime((Integer) claims.get("exp"))
                ? jwtUtil.generateToken(claims, 60 * 24)
                : refreshToken;
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkRemainTime(Integer exp) {
        Date expDate = new Date((long) exp * 1000);
        long diff = expDate.getTime() - System.currentTimeMillis();
        long diffMin = diff / (1000 * 60);
        return diffMin < 60; // 남은 시간 1시간 미만이면 true, 1시간 이상이면 false
    }

    // 토큰 만료 여부 확인 메서드 (만료=true, 만료X=false)
    private boolean checkExpiredToken(String accessToken) {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (CustomJWTException e) {
            if (e.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}
