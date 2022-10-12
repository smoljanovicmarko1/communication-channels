package com.remare.communicationchannels.dto;

import lombok.Data;

@Data
public class AccountVerificationRequest {
  private Long userId;
  private String token;
}
