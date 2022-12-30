package com.teamzero.member.domain.model.dto;

import lombok.Builder;
import lombok.Data;

public class SignUpDto {
  @Data
  @Builder
  public static class Response{
    private String email;
    private String nickname;
    private int age;
  }
  @Data
  public static class Request{
    private String email;
    private String nickname;
    private int age;
    private String password;
    private String rePassword;
  }
}
