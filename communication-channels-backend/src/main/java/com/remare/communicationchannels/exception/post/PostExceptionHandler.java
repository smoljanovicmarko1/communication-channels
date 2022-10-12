package com.remare.communicationchannels.exception.post;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

@RestControllerAdvice
public class PostExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({FileAlreadyExistsException.class})
  public ResponseEntity<Object> fileAlreadyExists(
      FileAlreadyExistsException e, WebRequest webRequest) {
    return handleExceptionInternal(
        e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
  }

  @ExceptionHandler({FileNotFoundException.class})
  public ResponseEntity<Object> fileNotFound(FileAlreadyExistsException e, WebRequest webRequest) {
    return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.OK, webRequest);
  }
}
