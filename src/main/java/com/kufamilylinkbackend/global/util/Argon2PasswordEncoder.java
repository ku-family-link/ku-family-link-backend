package com.kufamilylinkbackend.global.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2PasswordEncoder {

  private static final Argon2 argon2 = Argon2Factory.create();

  // 비밀번호 해싱
  public static String encode(char[] password) {
    return argon2.hash(2, 65536, 1, password); // Iterations, Memory, Parallelism
  }

  // 비밀번호 검증
  public static boolean matches(char[] rawPassword, String encodedPassword) {
    return argon2.verify(encodedPassword, rawPassword);
  }

  // 민감한 데이터 지우기
  public static void wipePassword(char[] password) {
    argon2.wipeArray(password);
  }
}
