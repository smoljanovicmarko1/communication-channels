package com.remare.communicationchannels.dto;

import com.remare.communicationchannels.dto.user.UserDto;
import lombok.Data;

@Data
public class MakeAccountRequestDto {
  private String token;
  private UserDto user;
}
