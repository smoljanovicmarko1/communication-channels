package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.user.UserDto;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

public interface EmailService {
  void sendPasswordToUserEmail(String email, String password);

  void sendAccountActivationLinkToUserEmail(UserDto savedUserDto)
      throws NoSuchAlgorithmException, MessagingException;
}
