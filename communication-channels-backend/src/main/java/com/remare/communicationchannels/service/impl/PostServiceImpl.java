package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.dto.post.PostDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.entity.Post;
import com.remare.communicationchannels.exception.post.PostNotFoundException;
import com.remare.communicationchannels.mapper.PostMapper;
import com.remare.communicationchannels.repository.PostRepository;
import com.remare.communicationchannels.service.CommentService;
import com.remare.communicationchannels.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final CommentService commentService;

  @Autowired
  public PostServiceImpl(
      PostRepository postRepository, PostMapper postMapper, CommentService commentService) {
    this.postRepository = postRepository;
    this.postMapper = postMapper;
    this.commentService = commentService;
  }

  @Override
  public List<PostDto> findAll() {
    return postMapper.toDtoList(postRepository.findAll());
  }

  @Override
  public PostDto findById(Long id) {
    return this.postMapper.toDto(
        postRepository
            .findById(id)
            .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found.")));
  }

  @Override
  public PostDto update(PostDto postDto) {
    Post post = postMapper.toEntity(postDto);
    return postMapper.toDto(postRepository.save(post));
  }

  @Override
  public PostDto save(PostDto postDto) {
    return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
  }

  @Transactional
  @Override
  public PostShortDto deleteById(Long id) {
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found."));
    post.getComments().forEach(comment -> commentService.deleteById(comment.getId()));
    postRepository.deleteById(id);
    return postMapper.toShortDto(post);
  }
}
