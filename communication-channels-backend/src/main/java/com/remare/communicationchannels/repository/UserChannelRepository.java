package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.UserChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserChannelRepository extends JpaRepository<UserChannel, Long> {

  List<UserChannel> findAllByUserId(Long userId);

  List<UserChannel> findAllByChannelId(Long channelId);

  @Query(
      "SELECT userChannel FROM UserChannel userChannel WHERE userChannel.channel.id = ?1 and userChannel.user.id <> ?2")
  Page<UserChannel> findAllByUserInChannel(Long channelId, Long loggedUserId, Pageable pageable);

  @Query(
      "SELECT userChannel FROM UserChannel userChannel group by userChannel.user HAVING "
          + "userChannel.user.id not in (select userChannel.user.id from UserChannel userChannel WHERE userChannel.channel.id = ?1)")
  Page<UserChannel> findAllByUserNotChannel(Long channelId, Long loggedUserId, Pageable pageable);

  UserChannel findByUserIdAndChannelId(Long userId, Long channelId);
}
