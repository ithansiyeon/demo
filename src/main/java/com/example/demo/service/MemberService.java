package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository; //스프링이 스프링 빈에 등록되어 있는 memberRepository를 injection 해줌

    @Transactional(readOnly=false)
    public Long join(Member member) {

        //validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    public void findOne(Long id) {
        memberRepository.findById(id);
    }
}
