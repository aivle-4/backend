package com.example.aivle.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 우선 다 BAD_REQUEST로 할게요. 나중에 코드에 맞게 바꾸시면 될 것 같아요.

    // auth 에러
    DUPLICATE_ID(BAD_REQUEST, "이미 존재하는 ID입니다."),
    NOT_FOUND_MEMBER(BAD_REQUEST, "존재하지 않는 아이디입니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Token 에러
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NOT_FOUND_TOKEN(UNAUTHORIZED, "토큰을 찾을 수 없습니다."),

    // book 에러
    NOT_FOUND_BOOK(BAD_REQUEST, "존재하지 않는 책입니다."),
    NOT_AVAILABLE_BOOK(BAD_REQUEST, "수정 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
