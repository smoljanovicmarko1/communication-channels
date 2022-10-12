package com.remare.communicationchannels.dto.like;

import com.remare.communicationchannels.dto.comment.CommentShortDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

  private Long id;
  private Date dateCreated;
  private LikeStatusDto likeStatus;
  private UserShortDto user;
  private PostShortDto post;
  private CommentShortDto comment;
}
