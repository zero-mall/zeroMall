package com.teamzero.member.domain.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {

    NO_AUTH("미인증"),
    IN_USE("활성화"),
    STOPPED("정지"),
    WITHDRAW("탈퇴");

    private final String description;

    public static boolean hasStatus(String status) {

        MemberStatus input = MemberStatus.valueOf(status);

        for (MemberStatus memberStatus : MemberStatus.values()) {
            if (memberStatus.equals(input)) {
                return true;
            }
        }

        return false;
    }

}
