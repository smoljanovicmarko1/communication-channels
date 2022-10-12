package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.like.LikeDto;

public interface LikeService {
  void deleteLike(Long id);

  LikeDto save(LikeDto likeDto);
}
