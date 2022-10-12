package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.LikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeStatusRepository extends JpaRepository<LikeStatus, Long> {
  Optional<LikeStatus> findByName(String name);
}
