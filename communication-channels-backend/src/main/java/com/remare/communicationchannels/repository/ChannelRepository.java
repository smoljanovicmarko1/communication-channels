package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
  Optional<Channel> findByName(String name);

  List<Channel> findByParentChannelId(Long id);

  @Query(
      value =
          "SELECT channel FROM Channel channel "
              + "JOIN UserChannel userChannel ON(userChannel.channel.id = channel.id) "
              + "WHERE channel.name LIKE %:filterValue% AND userChannel.user.id = :userId")
  List<Channel> findByFilterValue(
      @Param("filterValue") String filterValue, @Param("userId") Long userId);

  @Query(
      "SELECT channel.id FROM Channel channel JOIN Post post ON(channel.id = post.channel.id) WHERE post.id = ?1")
  Long findIdByPostId(Long postId);
}
