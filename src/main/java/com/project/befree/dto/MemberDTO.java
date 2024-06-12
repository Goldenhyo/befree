package com.project.befree.dto;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Slf4j
public class MemberDTO extends User {

    private String email;
    private String password;
    private String name;
    private boolean status;
    private boolean social;

    public MemberDTO(String email, String password,  String name, boolean status, boolean social) {
        super(email, password, new ArrayList<>());
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.social = social;
    }

    // 현재 사용자 정보를 Map 타입으로 리턴 (JWT 위한 메서드, 추후 JWT 문자열 생성시 사용)
    // MemberDTO 리턴시 User 포함하고 있어서 문제발생 가능 -> Map 타입으로 정보만 리턴
    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("status", status);
        map.put("social", social);
        return map;
    }
}
