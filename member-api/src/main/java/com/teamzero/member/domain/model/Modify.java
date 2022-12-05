package com.teamzero.member.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Modify {
    private Long memberId;
    private String nickname;
    private String password;

}
