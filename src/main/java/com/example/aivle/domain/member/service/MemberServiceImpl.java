package com.example.aivle.domain.member.service;

import com.example.aivle.domain.member.dto.LoginRequest;
import com.example.aivle.domain.member.dto.LoginResponse;
import com.example.aivle.domain.member.entity.Member;
import com.example.aivle.domain.member.repository.MemberRepository;
import com.example.aivle.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.aivle.global.response.ErrorCode.DUPLICATE_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse signup(LoginRequest request) {
        memberRepository.findByLoginId(request.loginId())
                .ifPresent(m -> {
                    throw new CustomException(DUPLICATE_ID);
                });

        String encodedPwd = passwordEncoder.encode(request.password());
        Member member = Member.builder()
                .loginId(request.loginId())
                .password(encodedPwd)
                .build();

        Member saved = memberRepository.save(member);
        return new LoginResponse(saved.getId());
    }
}
