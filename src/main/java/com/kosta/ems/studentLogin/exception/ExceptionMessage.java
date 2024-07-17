package com.kosta.ems.studentLogin.exception;

public enum ExceptionMessage {
    AUTHENTICATION_FAILED("인증 실패"),
    AUTHORIZATION_FAILED("접근 권한 없음"),
    INVALID_TOKEN("유효하지 않은 토큰"),
    EXPIRED_TOKEN("만료된 토큰"),
    ILLEGAL_ARGUMENT("적합하지 않은 인자");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

