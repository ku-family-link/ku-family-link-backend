package com.kufamilylinkbackend.global.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

  // 1000: Success Case
  SUCCESS(HttpStatus.OK, 1000, "정상적인 요청입니다."),
  CREATED(HttpStatus.CREATED, 1001, "정상적으로 생성되었습니다."),

  // 2000: Common Error
  INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 2000, "예기치 못한 오류가 발생했습니다."),
  NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 리소스입니다."),
  INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, 2002, "올바르지 않은 요청 값입니다."),
  UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, 2003, "권한이 없는 요청입니다."),
  ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, 2004, "이미 삭제된 리소스입니다."),
  FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, 2005, "인가되지 않는 요청입니다."),
  ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, 2006, "이미 존재하는 리소스입니다."),
  INVALID_SORT_EXCEPTION(HttpStatus.BAD_REQUEST, 2007, "올바르지 않은 정렬 값입니다."),
  POINT_NOT_ENOUGH_EXCEPTION(HttpStatus.BAD_REQUEST, 2008, "포인트가 부족합니다."),
  SEARCH_LOG_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 2009, "존재하지 않는 검색 로그입니다."),
  USER_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, 3000,
      "이미 존재하는 유저입니다."),
  GUARDIAN_NOT_FOUND_EXCEPTION(
      HttpStatus.NOT_FOUND, 3001, "유저가 존재하지 않습니다."),
  PASSWORD_MISMATCH_EXCEPTION(
      HttpStatus.UNAUTHORIZED, 3002, "비밀번호가 틀렸습니다."),
  FITBIT_USER_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST, 4000, "해당 fitbit 유저가 존재하지 않습니다.");


  private final HttpStatus httpStatus;
  private final Integer code;
  private final String message;

  ErrorCode(HttpStatus httpStatus, Integer code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }
}