package com.example.aivle.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // auth 에러
    DUPLICATE_ID(BAD_REQUEST, "이미 존재하는 ID입니다."),
    NOT_FOUND_LOGINID(BAD_REQUEST, "존재하지 않는 아이디입니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
