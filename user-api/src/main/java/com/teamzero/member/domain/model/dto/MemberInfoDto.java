package com.teamzero.member.domain.model.dto;

import com.teamzero.member.domain.model.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDto {

    private Long   memberId;
    private String email;
    private int age;
    private String nickname;
    private String grade;
    private String status;

    public static MemberInfoDto fromEntity(MemberEntity member) {
        return MemberInfoDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .age(member.getAge())
                .nickname(member.getNickname())
                .grade(member.getMemberGradeEntity().getName())
                .status(String.valueOf(member.getMemberStatus()))
                .build();
    }
}
