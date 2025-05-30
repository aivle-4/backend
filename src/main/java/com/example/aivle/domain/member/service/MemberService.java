package com.example.aivle.domain.member.service;

import com.example.aivle.domain.member.dto.LoginRequest;
import com.example.aivle.domain.member.dto.LoginResponse;
import com.example.aivle.domain.member.entity.Member;

public interface MemberService {

    LoginResponse signup(LoginRequest request);

    LoginResponse login(LoginRequest request);

    Member findMember(Integer memberId);
}
