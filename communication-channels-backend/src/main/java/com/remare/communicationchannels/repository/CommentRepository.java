package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  void deleteByPostId(Long postId);

  List<Comment> findAllByCommentId(Long id);
}
