package com.teamzero.member.domain.model.dto;

import lombok.Data;

@Data
public class SignUp {
  private String email;
  private String nickname;
  private int age;
  private String password;
  private String rePassword;
}
