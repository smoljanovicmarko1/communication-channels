package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentStatusRepository extends JpaRepository<CommentStatus, Long> {
  Optional<CommentStatus> findByName(String name);
}
