package com.teamzero.member.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyDto {
    private Long memberId;
    private String nickname;
    private int age;
    private String password;

    private String grade;
    private String status;
}
