package com.teamzero.member.domain.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Modify {

    private Long memberId;
    private String nickname;
    private String password;

    private String grade;
    private String status;

}
