package com.project.befree.service;

import com.project.befree.domain.Member;
import com.project.befree.dto.MemberDTO;
import com.project.befree.dto.MemberFormDTO;

import java.util.Map;

public interface MemberService {

    Member getOne(String email);

    Map<String, String> save(MemberDTO memberDTO);

    MemberDTO getKakaoMember(String accessToken);

    void delete(MemberDTO memberDTO, String memberEmail);

    void modify(MemberDTO memberDTO, String memberEmail);

}
