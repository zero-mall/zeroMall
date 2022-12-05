package com.teamzero.member.domain.model.dto;

import com.teamzero.member.domain.model.MemberEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfo {

    private Long   memberId;
    private String email;
    private String nickname;
    private String grade;
    private String status;

    public static MemberInfo fromEntity(MemberEntity member) {
        return MemberInfo.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .grade(member.getMemberGradeEntity().getName())
                .status(String.valueOf(member.getMemberStatus()))
                .build();
    }
}
