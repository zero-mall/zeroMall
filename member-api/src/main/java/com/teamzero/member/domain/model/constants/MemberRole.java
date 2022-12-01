package com.teamzero.member.domain.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    REGULAR("일반 회원"),
    ADMIN("관리자");

    private final String description;

}
