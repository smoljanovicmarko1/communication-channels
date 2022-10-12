package com.remare.communicationchannels.validator.user.email;

import com.remare.communicationchannels.exception.user.UserNotFoundException;
import com.remare.communicationchannels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotTakenValidator implements ConstraintValidator<EmailNotTaken, String> {

  private final UserService userService;

  @Autowired
  public EmailNotTakenValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void initialize(EmailNotTaken constraintAnnotation) {}

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    try {
      userService.findByEmail(email);
      return false;
    } catch (UserNotFoundException e) {
      return true;
    }
  }
}
