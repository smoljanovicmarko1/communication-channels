package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.constant.ExceptionConstant;
import com.remare.communicationchannels.dto.comment.CommentDto;
import com.remare.communicationchannels.dto.comment.CommentStatusDto;
import com.remare.communicationchannels.dto.like.LikeStatusDto;
import com.remare.communicationchannels.exception.comment.CommentNotFountException;
import com.remare.communicationchannels.exception.comment.LikeStatusNotFoundException;
import com.remare.communicationchannels.mapper.CommentMapper;
import com.remare.communicationchannels.mapper.LikeMapper;
import com.remare.communicationchannels.repository.CommentRepository;
import com.remare.communicationchannels.repository.CommentStatusRepository;
import com.remare.communicationchannels.repository.LikeStatusRepository;
import com.remare.communicationchannels.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;
  private final LikeMapper likeMapper;
  private final CommentStatusRepository commentStatusRepository;
  private final LikeStatusRepository likeStatusRepository;

  @Autowired
  public CommentServiceImpl(
      CommentRepository commentRepository,
      CommentMapper commentMapper,
      LikeMapper likeMapper,
      CommentStatusRepository commentStatusRepository,
      LikeStatusRepository likeStatusRepository) {
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
    this.likeMapper = likeMapper;
    this.commentStatusRepository = commentStatusRepository;
    this.likeStatusRepository = likeStatusRepository;
  }

  @Override
  public CommentDto findById(Long id) {
    return commentMapper.toDto(
        commentRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new CommentNotFountException(
                        String.format(ExceptionConstant.COMMENT_NOT_FOUND_BY_ID, id))));
  }

  @Override
  public CommentStatusDto findCommentStatusByName(String name) {
    return commentMapper.toCommentStatusDto(
        commentStatusRepository
            .findByName(name)
            .orElseThrow(
                () -> new CommentNotFountException("Comment with name " + name + " not found.")));
  }

  @Override
  public CommentDto save(CommentDto commentDto) {
    return commentMapper.toDto(commentRepository.save(commentMapper.toEntity(commentDto)));
  }

  @Override
  public LikeStatusDto findLikeStatusByName(String likeStatusString) {
    return likeMapper.toStatusDto(
        likeStatusRepository
            .findByName(likeStatusString)
            .orElseThrow(
                () ->
                    new LikeStatusNotFoundException(
                        "Like Status with like status " + likeStatusString + " no found.")));
  }

  @Override
  public CommentDto deleteById(Long id) {
    CommentDto commentDto =
        commentMapper.toDto(
            commentRepository
                .findById(id)
                .orElseThrow(
                    () -> new CommentNotFountException("Comment with " + id + " no found.")));
    commentRepository.findAllByCommentId(id).forEach(comment -> deleteById(comment.getId()));
    commentRepository.deleteById(id);
    return commentDto;
  }
}
