package com.project.befree.service;

import com.project.befree.domain.Member;
import com.project.befree.dto.MemberDTO;
import com.project.befree.dto.MemberFormDTO;
import com.project.befree.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public Member getOne(String email) {
        Optional<Member> memberOptional = memberRepository.findById(email);
        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            return null;
        }
    }

    public Map<String, String> save(MemberDTO memberDTO) {
        Member member = modelMapper.map(memberDTO, Member.class);
        Member saved = memberRepository.save(member);
        return Map.of("success", saved.getName());
    }

    @Override
    public void delete(MemberDTO memberDTO, String memberEmail) {
        log.info("************MemberServiceImpl delete memberFormDTO:{}", memberDTO);
        Member member = memberRepository.findById(memberEmail).orElseThrow();
        member.changeStatus((Boolean) memberDTO.getClaims().get("status"));
        memberRepository.save(member);
    }

    @Override
    public void modify(MemberDTO memberDTO, String memberEmail) {
        log.info("************MemberServiceImpl modify memberFormDTO:{}", memberDTO);
        Member member = memberRepository.findById(memberEmail).orElseThrow();
        member.changePassword(memberDTO.getPassword());
        memberRepository.save(member);
    }
}
