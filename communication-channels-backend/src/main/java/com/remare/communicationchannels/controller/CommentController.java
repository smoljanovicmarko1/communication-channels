package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.dto.comment.CommentDto;
import com.remare.communicationchannels.dto.comment.CommentStatusDto;
import com.remare.communicationchannels.dto.like.LikeStatusDto;
import com.remare.communicationchannels.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping("/{id}")
  public CommentDto findById(@PathVariable("id") Long id) {
    return commentService.findById(id);
  }

  @GetMapping("/comment-status/{name}")
  public CommentStatusDto findCommentStatusByName(@PathVariable("name") String name) {
    return commentService.findCommentStatusByName(name);
  }

  @PostMapping("/addReplay")
  public CommentDto addReplay(@RequestBody CommentDto commentDto) {
    return this.commentService.save(commentDto);
  }

  @GetMapping("/like-status/{likeStatusString}")
  public LikeStatusDto findLikeStatusByName(
      @PathVariable("likeStatusString") String likeStatusString) {
    return this.commentService.findLikeStatusByName(likeStatusString);
  }

  @PostMapping("/addLike")
  public CommentDto addLike(@RequestBody CommentDto commentDto) {
    return this.commentService.save(commentDto);
  }

  @PostMapping("/save")
  public CommentDto save(@RequestBody CommentDto commentDto) {
    return this.commentService.save(commentDto);
  }
}
