package com.example.aivle.domain.member.service;

import com.example.aivle.domain.member.dto.LoginRequest;
import com.example.aivle.domain.member.dto.LoginResponse;

public interface MemberService {

    LoginResponse signup(LoginRequest loginRequest);

}
