package com.example.aivle.domain.member.controller;

import com.example.aivle.domain.member.dto.LoginRequest;
import com.example.aivle.domain.member.dto.LoginResponse;
import com.example.aivle.domain.member.service.MemberService;
import com.example.aivle.global.response.Response;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/new")
    public Response<LoginResponse> signup(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = memberService.signup(request);
        session.setAttribute("memberId", response.memberId());
        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = memberService.login(request);
        session.setAttribute("memberId", response.memberId());
        return Response.success(response);
    }
}
