package com.remare.communicationchannels.exception.user;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

public class InvalidAccountActivationTokenException extends InvalidTokenException {

  public InvalidAccountActivationTokenException(String msg) {
    super(msg);
  }
}
