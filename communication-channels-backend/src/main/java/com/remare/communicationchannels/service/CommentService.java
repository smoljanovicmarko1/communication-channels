package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.comment.CommentDto;
import com.remare.communicationchannels.dto.comment.CommentStatusDto;
import com.remare.communicationchannels.dto.like.LikeStatusDto;

public interface CommentService {

  CommentDto findById(Long id);

  CommentStatusDto findCommentStatusByName(String name);

  CommentDto save(CommentDto commentDto);

  LikeStatusDto findLikeStatusByName(String likeStatusString);

  CommentDto deleteById(Long id);
}
