package com.project.befree.controller;

import com.project.befree.domain.Member;
import com.project.befree.dto.EmailMsgDTO;
import com.project.befree.dto.MemberDTO;
import com.project.befree.dto.MemberFormDTO;
import com.project.befree.dto.MemberConfirmDTO;
import com.project.befree.service.EmailService;
import com.project.befree.service.MemberService;
import com.project.befree.util.CustomJWTException;
import com.project.befree.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final EmailService emailService;

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

    @PutMapping("/confirm")
    public String confirm(@RequestBody MemberConfirmDTO dto, @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);
        Map<String, Object> claims = jwtUtil.validateToken(accessToken);
        log.info("******************* MemberController confirm dto:{}", dto);
        String memberEmail = (String) claims.get("email");
        Member findMember = memberService.getOne(memberEmail);
        if (findMember == null) {
            return "fail";
        }
        if (passwordEncoder.matches(dto.getPassword(), findMember.getPassword())) {
            if (dto.getResult().equals("delete")) {
                MemberDTO memberDTO =
                        new MemberDTO(memberEmail,
                                passwordEncoder.encode(dto.getPassword()),
                                (String) claims.get("name"),
                                false, false);
                memberService.delete(memberDTO, memberEmail);
                log.info("-----------탈퇴완료");
            }
            log.info("-----------------비밀번호 일치");
            return "success";
        }
        log.info("실패");
        return "fail";
    }

    @PutMapping("/modify")
    public String modify(@RequestBody MemberConfirmDTO dto, @RequestHeader("Authorization") String authHeader) {
        log.info("**************MemberController modify dto:{}", dto);
        String accessToken = authHeader.substring(7);
        Map<String, Object> claims = jwtUtil.validateToken(accessToken);
        String memberEmail = (String) claims.get("email");
        log.info("**************MemberController modify memberEmail:{}", memberEmail);
        MemberDTO memberDTO =
                new MemberDTO(memberEmail,
                        passwordEncoder.encode(dto.getPassword()),
                        (String) claims.get("name"),
                        true, false);
        memberService.modify(memberDTO, memberEmail);
        return "haha";
    }

    @PostMapping("/email")
    public Map<String, String> sendEmail(@RequestBody String str) {
        String str2 = str.substring(9);
        String email = str2.split("\"")[1];
        log.info("************ MemberController sendEmail:{}", email);
        EmailMsgDTO emailMessage = EmailMsgDTO.builder()
                .to(email)
                .subject("BeFree 테스트메일")
                .message("이메일 인증을 위한 이메일입니다.")
                .build();
        Map<String, String> result = emailService.sendMail(emailMessage);
        String key = result.get("key");
        log.info("*********************이메일인증번호:{}", key);
        return Map.of("key", key);
    }

    @PostMapping("/password")
    public String newPassword(@RequestBody String str) {
        log.info("************ MemberController newPassword:{}", str);
        String str2 = str.substring(9);
        String email = str2.split("\"")[1];
        Member findMember = memberService.getOne(email);
        if (findMember == null) {
            return "fail";
        }
        EmailMsgDTO emailMessage = EmailMsgDTO.builder()
                .to(email)
                .subject("BeFree 테스트메일")
                .build();
        Map<String, String> result = emailService.sendMail(emailMessage);
        String newPw = result.get("key");

        MemberDTO memberDTO =
                new MemberDTO(email,
                        passwordEncoder.encode(newPw),
                        findMember.getName(),
                        findMember.isStatus(), false);
        memberService.modify(memberDTO, email);
        return "success";
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
