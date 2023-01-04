package com.teamzero.product.exception;

import static com.teamzero.product.exception.ErrorCode.INTERNAL_SERVER_EXCEPTION;

import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
  예외 처리
  - 제로몰 전용 예외 및 자주 발생하는 예외 처리
  - 참고 : 자주 발생하는 예외 https://www.baeldung.com/spring-exceptions
*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * 상품 로직 관련
   */
  @ExceptionHandler(value = TeamZeroException.class)
  public ResponseEntity<?> handleMemberException(TeamZeroException e) {

    ErrorCode code = e.getErrorCode();

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    return new ResponseEntity(response, code.getHttpStatus());
  }

  /**
   * 데이터 관련
   */
  @ExceptionHandler(value = DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  @ExceptionHandler(value = NonTransientDataAccessException.class)
  public ResponseEntity<?> handleNonTransientDataAccessException(
      NonTransientDataAccessException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  /**
   * Bean 관련
   */
  @ExceptionHandler(value = NoSuchBeanDefinitionException.class)
  public ResponseEntity<?> handleNoSuchBeanDefinitionException(
      NoSuchBeanDefinitionException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(e.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  @ExceptionHandler(value = BeansException.class)
  public ResponseEntity<?> handleBeansException(BeansException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  /**
   * 자주 발생하는 자바 런타임 예외
   */
  @ExceptionHandler(value = NullPointerException.class)
  public ResponseEntity<?> handleNullPointerException(NullPointerException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  @ExceptionHandler(value = ArrayIndexOutOfBoundsException.class)
  public ResponseEntity<?> handleArrayIndexOutOfBoundsException(
      ArrayIndexOutOfBoundsException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  @ExceptionHandler(value = FileNotFoundException.class)
  public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

  @ExceptionHandler(value = IOException.class)
  public ResponseEntity<?> handleIOException(IOException e) {

    ErrorCode code = INTERNAL_SERVER_EXCEPTION;

    ErrorResponse response = ErrorResponse.builder()
        .code(code.name())
        .message(code.getMessage())
        .build();

    log.error(e.getMessage());

    return new ResponseEntity(response, code.getHttpStatus());
  }

}
