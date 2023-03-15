package com.remare.communicationchannels.validator.channel.name.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatorWrapper {
  private String type;
  private String message;
}
