package com.teamzero.member.domain.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminStatus {

    IN_USE("사용중"),
    STOPPED("사용중지");

    private final String description;

}
