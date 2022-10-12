package com.remare.communicationchannels.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phone;
}
