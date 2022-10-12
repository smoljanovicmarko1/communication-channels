package com.remare.communicationchannels.dto.comment;

import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.dto.like.LikeDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

  private Long id;
  private String text;
  private Date dateCreated;
  private Date dateLastModified;
  private UserShortDto user;
  private CommentStatusDto commentStatus;
  private List<LikeDto> likes;
  private List<AttachmentDto> attachments;
  private List<CommentDto> comments;
  private PostShortDto post;
  private CommentShortDto comment;
}
