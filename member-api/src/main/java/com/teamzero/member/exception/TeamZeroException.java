package com.teamzero.member.exception;

import lombok.Getter;

@Getter
public class TeamZeroException extends RuntimeException {

    ErrorCode errorCode;

    public TeamZeroException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
