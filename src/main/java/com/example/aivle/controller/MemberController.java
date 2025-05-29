package com.example.aivle.controller;

import com.example.aivle.entity.Member;
import com.example.aivle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    @PostMapping("/new")
    public Map<String, Object> signup(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        Member newMember = Member.builder()
                .loginId(loginId)
                .password(password)
                .build();

        memberRepository.save(newMember);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", true);
        response.put("message", "회원가입이 완료되었습니다.");
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .map(member -> {
                    Map<String, Object> res = new HashMap<>();
                    res.put("isSuccess", true);
                    res.put("message", "성공입니다.");
                    res.put("result", Map.of("memberId", member.getId()));
                    return res;
                })
                .orElseGet(() -> {
                    Map<String, Object> res = new HashMap<>();
                    res.put("isSuccess", false);
                    res.put("message", "비밀번호가 일치하지 않습니다.");
                    res.put("result", null);
                    return res;
                });
    }
}
