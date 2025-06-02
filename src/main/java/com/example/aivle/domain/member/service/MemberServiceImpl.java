package com.example.aivle.domain.member.service;

import com.example.aivle.domain.member.dto.LoginRequest;
import com.example.aivle.domain.member.dto.LoginResponse;
import com.example.aivle.domain.member.entity.Member;
import com.example.aivle.domain.member.repository.MemberRepository;
import com.example.aivle.global.response.CustomException;
import com.example.aivle.global.util.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.aivle.global.response.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public LoginResponse signup(LoginRequest request) {
        memberRepository.findByLoginId(request.loginId())
                .ifPresent(m -> {
                    throw new CustomException(DUPLICATE_ID);
                });

        Member member = Member.builder()
                .loginId(request.loginId())
                .password(passwordEncoder.encode(request.password()))
                .build();

        Member saved = memberRepository.save(member);

        Authentication authentication = jwtTokenUtils.createAuthentication(saved.getId());
        String token = jwtTokenUtils.generateAccessToken(authentication);

        return new LoginResponse(saved.getId(), "Bearer", token);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId()).orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }
        Authentication authentication = jwtTokenUtils.createAuthentication(member.getId());
        String token = jwtTokenUtils.generateAccessToken(authentication);

        return new LoginResponse(member.getId(), "Bearer", token);
    }

    @Transactional(readOnly = true)
    @Override
    public Member findMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(INVALID_TOKEN, "인증되지 않은 사용자입니다.");
        }

        Integer memberId = Integer.parseInt(authentication.getName());
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }

}
