package com.teamzero.member.domain.model.dto;

import com.teamzero.member.domain.model.AdminEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInfo {

  private Long adminId;
  private String email;
  private String status;

  public static AdminInfo fromEntity(AdminEntity admin) {
    return AdminInfo.builder()
        .adminId(admin.getAdminId())
        .email(admin.getEmail())
        .status(String.valueOf(admin.getAdminStatus()))
        .build();
  }
}