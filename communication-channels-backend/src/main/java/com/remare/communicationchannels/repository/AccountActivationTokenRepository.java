package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.AccountActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountActivationTokenRepository
    extends JpaRepository<AccountActivationToken, Long> {

  Optional<AccountActivationToken> findByUserIdAndToken(Long userId, String token);
}
