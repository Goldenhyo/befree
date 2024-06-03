package com.project.befree.service;

import com.project.befree.domain.Member;
import com.project.befree.dto.MemberDTO;

import java.util.Map;

public interface MemberService {

    Member getOne(String email);

    Map<String, String> save(MemberDTO memberDTO);
}
