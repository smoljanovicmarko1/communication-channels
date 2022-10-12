package com.remare.communicationchannels.exception.user.handler;

import com.remare.communicationchannels.exception.user.InvalidAccountActivationTokenException;
import com.remare.communicationchannels.exception.user.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({UserNotFoundException.class})
  public ResponseEntity<Object> internalServerErrorException(Exception e, WebRequest webRequest) {
    return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.OK, webRequest);
  }

  @ExceptionHandler({InvalidAccountActivationTokenException.class})
  public ResponseEntity<Object> handleTokenException(Exception e, WebRequest webRequest) {
    return handleExceptionInternal(
        e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
  }
}
