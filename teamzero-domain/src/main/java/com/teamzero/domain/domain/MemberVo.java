package com.teamzero.domain.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberVo {

    private Long memberId;
    private String email;
    private String role;
    private String grade;

}
