package com.remare.communicationchannels.dto.attachment;

import com.remare.communicationchannels.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {

  private Long id;
  private String originalName;
  private UserShortDto user;
}
