package com.example.aivle.domain.member.dto;

public record LoginResponse(Integer memberId, String type, String accessToken) {
}
