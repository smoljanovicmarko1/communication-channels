package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
  //    @Query("SELECT post FROM Post post WHERE post.title LIKE %:filterValue%")
  @Query(
      "SELECT post FROM Post post "
          + "JOIN Channel channel ON(post.channel.id = channel.id) "
          + "JOIN UserChannel userChannel ON(channel.id = userChannel.channel.id) "
          + "WHERE userChannel.user.id = :userId AND post.title LIKE %:filterValue%")
  List<Post> findByFilterValue(
      @Param("filterValue") String filterValue, @Param("userId") Long userId);
}
