package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  @Query(
      "select u from User u where u.id not in (select uc.user.id from UserChannel uc where uc.channel.id = ?1 or uc.user.id = ?2)")
  Page<User> findAllNotInChannel(Long channelId, Long loggedUserId, Pageable pageable);
}
