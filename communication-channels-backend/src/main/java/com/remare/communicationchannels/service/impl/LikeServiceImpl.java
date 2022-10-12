package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.dto.like.LikeDto;
import com.remare.communicationchannels.mapper.LikeMapper;
import com.remare.communicationchannels.repository.LikeRepository;
import com.remare.communicationchannels.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;
  private final LikeMapper likeMapper;

  @Autowired
  public LikeServiceImpl(LikeRepository likeRepository, LikeMapper likeMapper) {
    this.likeRepository = likeRepository;
    this.likeMapper = likeMapper;
  }

  @Override
  public void deleteLike(Long id) {
    this.likeRepository.deleteById(id);
  }

  @Override
  public LikeDto save(LikeDto likeDto) {
    return likeMapper.toDto(likeRepository.save(likeMapper.toEntity(likeDto)));
  }
}
