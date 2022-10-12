package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.constant.AccountActivationConstant;
import com.remare.communicationchannels.constant.EmailConstant;
import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.entity.AccountActivationToken;
import com.remare.communicationchannels.entity.User;
import com.remare.communicationchannels.repository.AccountActivationTokenRepository;
import com.remare.communicationchannels.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender emailSender;
  private final AccountActivationTokenRepository accountActivationTokenRepository;

  @Autowired
  public EmailServiceImpl(
      JavaMailSender emailSender,
      AccountActivationTokenRepository accountActivationTokenRepository) {
    this.emailSender = emailSender;
    this.accountActivationTokenRepository = accountActivationTokenRepository;
  }

  @Override
  public void sendPasswordToUserEmail(String email, String password) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("knowledge.helper112@gmail.com");
    message.setTo(email);
    message.setSubject(EmailConstant.EMAIL_SEND_PASSWORD_SUBJECT);
    message.setText(String.format(EmailConstant.EMAIL_SEND_PASSWORD_BODY_MESSAGE, password));
    emailSender.send(message);
  }

  @Override
  public void sendAccountActivationLinkToUserEmail(UserDto savedUserDto)
      throws NoSuchAlgorithmException, MessagingException {

    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom("knowledge.helper112@gmail.com");
    message.setTo(savedUserDto.getEmail());
    message.setSubject(EmailConstant.SEND_ACTIVATION_SUBJECT);

    String token = generateAccountActivationToken(savedUserDto.getId());

    String activationLink = createActivationLink(savedUserDto.getId(), token);

    saveAccountActivationToken(savedUserDto, token);

    message.setText(String.format(EmailConstant.SEND_ACTIVATION_LINK, activationLink));
    emailSender.send(message);
  }

  private void saveAccountActivationToken(UserDto savedUserDto, String token) {
    User user = new User();
    user.setId(savedUserDto.getId());

    AccountActivationToken accountActivationToken = new AccountActivationToken();
    accountActivationToken.setUser(user);
    accountActivationToken.setToken(token);

    accountActivationTokenRepository.save(accountActivationToken);
  }

  private String createActivationLink(Long id, String token) throws NoSuchAlgorithmException {
    StringBuilder builder = new StringBuilder();

    return builder
        .append(EmailConstant.CLIENT_ROOT_URL)
        .append("make-account")
        .append("/")
        .append(id)
        .append("/")
        .append(token)
        .toString();
  }

  private String generateAccountActivationToken(Long id) throws NoSuchAlgorithmException {
    int leftLimit = 33;
    int rightLimit = 122;
    int targetStringLength = AccountActivationConstant.PRIVATE_KEY_LENGTH;
    Random random = new SecureRandom();

    String generatedString =
        random
            .ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

    MessageDigest md = MessageDigest.getInstance("SHA-512");

    byte[] messageDigest = md.digest(generatedString.getBytes());

    BigInteger no = new BigInteger(1, messageDigest);

    String result = no.toString(16);

    while (result.length() < 32) {
      result = "0" + result;
    }

    return result;
  }
}
