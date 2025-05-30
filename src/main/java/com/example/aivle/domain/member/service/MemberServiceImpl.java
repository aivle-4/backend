package com.example.aivle.domain.member.service;

import com.example.aivle.domain.member.dto.LoginRequest;
import com.example.aivle.domain.member.dto.LoginResponse;
import com.example.aivle.domain.member.entity.Member;
import com.example.aivle.domain.member.repository.MemberRepository;
import com.example.aivle.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.aivle.global.response.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public LoginResponse signup(LoginRequest request) {
        memberRepository.findByLoginId(request.loginId())
                .ifPresent(m -> {
                    throw new CustomException(DUPLICATE_ID);
                });

        Member member = Member.builder()
                .loginId(request.loginId())
                .password(request.password())
                .build();

        Member saved = memberRepository.save(member);
        return new LoginResponse(saved.getId());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId()).orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
        if (!member.getPassword().equals(request.password())) {
            throw new CustomException(INVALID_PASSWORD);
        }
        return new LoginResponse(member.getId());
    }
}
