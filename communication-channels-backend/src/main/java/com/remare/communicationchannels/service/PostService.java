package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.post.PostDto;
import com.remare.communicationchannels.dto.post.PostShortDto;

import java.util.List;

public interface PostService {

  List<PostDto> findAll();

  PostDto findById(Long id);

  PostDto update(PostDto postDto);

  PostDto save(PostDto postDto);

  PostShortDto deleteById(Long id);
}
