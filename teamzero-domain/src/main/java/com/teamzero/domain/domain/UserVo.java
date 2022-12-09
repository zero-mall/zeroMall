package com.teamzero.domain.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {

    private Long memberId;
    private String email;
    private String grade;

}
