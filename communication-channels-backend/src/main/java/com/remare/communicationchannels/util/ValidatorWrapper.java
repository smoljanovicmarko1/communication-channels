package com.remare.communicationchannels.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatorWrapper {
  private String type;
  private String message;
}
