package com.teamzero.member.domain.model;

import lombok.Data;

@Data
public class SignUp {
  private String email;
  private String nickname;
  private String password;
  private String rePassword;
}
