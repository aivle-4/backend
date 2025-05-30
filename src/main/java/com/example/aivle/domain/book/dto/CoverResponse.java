package com.example.aivle.domain.book.dto;

public record CoverResponse(
        boolean isSuccess,
        String message,
        Result result
) {
    public record Result(String coverImageUrl) {}

    public static CoverResponse ok(String url) {
        return new CoverResponse(true, "성공입니다.", new Result(url));
    }
}