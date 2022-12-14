package com.teamzero.product.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 카테고리 관련 에러 코드
    CATEGORY_NAME_DUPLICATE("중복된 카테고리명이 존재합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_SEARCH_BAD_REQEUST("요청값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_ID_FULL_ERROR("카테고리ID를 더이상 추가할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_SUB_DATA_EXISTS("하위 카테고리가 존재합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_PARANTID_ERROR("부모 카테고리Id가 없습니다.", HttpStatus.BAD_REQUEST),

    // 상품 관련 에러 코드
    PRODUCT_NOT_FOUND("해당이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_PRICE_INFO_NOT_EXIST("해당 상품의 가격정보를 조회할 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_REDIS_SAVE_FAIL("레디스에 데이터 저장을 실패했습니다.", HttpStatus.BAD_REQUEST),

    // 쇼핑몰 관련 에러 코드
    MALLID_NOT_FOUND("쇼핑몰 아이디가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALL_NOT_FOUND("해당 쇼핑몰 데이터가 존재하지 않습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // 리뷰 관련 에러 코드
    REVIEW_ALREADY_EXISTS("리뷰를 이미 작성한 상품입니다.", HttpStatus.BAD_REQUEST),
    REVIEW_DOES_NOT_EXISTS("존재하지 않는 리뷰입니다.", HttpStatus.BAD_REQUEST),

    // 별점 관련 에러 코드
    STAR_MEMBER_NOT_FOUND("해당 회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    STAR_SCORE_RANGE_NOT_VALID("별점은 1점 ~ 5점 사이로만 등록 가능합니다.", HttpStatus.BAD_REQUEST),

    // 좋아요 관련 에러 코드
    LIKE_MEMBER_NOT_FOUND("해당 회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    LIKE_ALREADY_LIKED("이미 좋아요를 누른 상품입니다.", HttpStatus.BAD_REQUEST),
    LIKE_ALREADY_UNLIKED("이미 좋아요를 취소한 상품입니다.", HttpStatus.BAD_REQUEST),

    // 조회수 관련 에러 코드
    VIEW_SEARCH_BAD_REQUEST("조회수 요청값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 메일 전송 관련 에러 코드
    MAIL_SEND_FAIL("메일 전송에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 기타 예외 처리
    INTERNAL_SERVER_EXCEPTION("알 수 없는 에러가 발생했습니다. "
        + "자세한 사항은 고객센터를 통해 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
