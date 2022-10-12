package com.remare.communicationchannels.constant;

public interface EmailConstant {

  String EMAIL_SEND_PASSWORD_SUBJECT = "Knowledge helper - new account password";
  String EMAIL_SEND_PASSWORD_BODY_MESSAGE = "Your new password is %s. \n The support team.";
  String SEND_ACTIVATION_SUBJECT = "Knowledge helper - account activation";
  String SEND_ACTIVATION_LINK =
      "Your account has been created. Visit link below to activate it.\n  The support team."
          + "\n\nActivation link: \n "
          + "%s";

  String CLIENT_ROOT_URL = "http://localhost:4200/";
}
