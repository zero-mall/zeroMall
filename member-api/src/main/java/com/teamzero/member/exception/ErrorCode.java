package com.teamzero.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND("해당 회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_SIGNUP_EMAIL_DUPLICATE("중복된 이메일이 존재합니다.", HttpStatus.BAD_REQUEST),
    MEMBER_SIGNUP_SEND_AUTH_EMAIL_FAIL("인증키 이메일전송에 실패 하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_EMAIL_AUTH_NOT_FOUND("인증키정보가 잘못 되었습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
