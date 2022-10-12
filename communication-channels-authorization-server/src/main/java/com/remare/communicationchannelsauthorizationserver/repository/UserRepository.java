package com.remare.communicationchannelsauthorizationserver.repository;

import com.remare.communicationchannelsauthorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
