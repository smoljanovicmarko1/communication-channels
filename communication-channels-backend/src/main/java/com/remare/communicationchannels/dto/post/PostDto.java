package com.remare.communicationchannels.dto.post;

import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.dto.channel.ChannelShortDto;
import com.remare.communicationchannels.dto.comment.CommentShortDto;
import com.remare.communicationchannels.dto.like.LikeDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

  private Long id;
  private String title;
  private String body;
  private Date dateCreated;
  private UserShortDto user;
  private ChannelShortDto channel;
  private List<LikeDto> likes;
  private List<CommentShortDto> comments;
  private List<AttachmentDto> attachments;
  private boolean edited;
}
