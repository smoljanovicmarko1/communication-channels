package com.remare.communicationchannels.dto.comment;

import com.remare.communicationchannels.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {
  private Long id;
  private String text;
  private Date dateCreated;
  private Date dateLastModified;
  private UserShortDto user;
  private CommentStatusDto commentStatus;
}
