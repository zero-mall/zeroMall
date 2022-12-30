package com.teamzero.member.domain.model.dto;

import com.teamzero.member.domain.model.AdminEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInfoDto {

  private Long adminId;
  private String email;
  private String status;

  public static AdminInfoDto fromEntity(AdminEntity admin) {
    return AdminInfoDto.builder()
        .adminId(admin.getAdminId())
        .email(admin.getEmail())
        .status(String.valueOf(admin.getAdminStatus()))
        .build();
  }
}