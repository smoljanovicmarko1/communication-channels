package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  Optional<UserRole> findByName(String name);
}
