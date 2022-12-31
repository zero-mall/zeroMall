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
    MEMBER_EMAIL_AUTH_NOT_FOUND("인증키정보가 잘못 되었습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_SIGNIN_NOT_POSSIBLE("회원 아이디가 없거나 비밀번호가 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    MEMBER_GRADE_NOT_FOUND("해당 회원 등급은 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_STATUS_NOT_EXIST("해당 회원 상태는 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_DATA_NOT_FOUND("검색 결과 데이터가 없습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("해당 상품이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_VALID("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    SUBSCRIBE_TASK_CAN_NOT_BE_DONE("구독작업을 완료 할 수 없습니다",HttpStatus.BAD_REQUEST),

    // 기타 예외 처리
    INTERNAL_SERVER_EXCEPTION("알 수 없는 에러가 발생했습니다. "
        + "자세한 사항은 고객센터를 통해 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
