package com.project.befree.service;

import com.project.befree.domain.Member;
import com.project.befree.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public Member getOne(String email){
        Optional<Member> memberOptional = memberRepository.findById(email);
        if(memberOptional.isPresent()) {
            return memberOptional.get();
        }else{
            log.error("Member with email {} not found", email);
            return null;
        }
    }
}
