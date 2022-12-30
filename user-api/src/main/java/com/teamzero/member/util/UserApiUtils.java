package com.teamzero.member.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * User-Api에서 사용하는 공통 메서드 UtilClass
 */
public class UserApiUtils {
  /**
   * 비밀번호 암호화
   */
  public static String createEncPassword(String plainText) {
    return BCrypt.hashpw(plainText, BCrypt.gensalt());
  }

  /**
   * 비밀번호 체크
   */
  public static boolean checkEncPassword(String passwd, String rePasswd){
    return BCrypt.checkpw(passwd, rePasswd);
  }
}
