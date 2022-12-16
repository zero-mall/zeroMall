package com.teamzero.product.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    CATEGORY_NAME_DUPLICATE("중복된 카테고리명이 존재합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_PARAMETER_ERROR("요청값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_ID_FULL_ERROR("카테고리ID를 더이상 추가할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_SUB_DATA_EXISTS("하위 카테고리가 존재합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_PARANTID_ERROR("부모 카테고리Id가 없습니다.", HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTS("리뷰를 이미 작성한 상품입니다.", HttpStatus.BAD_REQUEST),
    REVIEW_DOES_NOT_EXISTS("존재하지 않는 리뷰입니다.", HttpStatus.BAD_REQUEST),
    MALLID_NOT_FOUND("쇼핑몰 아이디가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
