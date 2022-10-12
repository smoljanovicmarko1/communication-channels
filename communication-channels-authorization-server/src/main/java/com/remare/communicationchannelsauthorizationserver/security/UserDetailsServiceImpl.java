package com.remare.communicationchannelsauthorizationserver.security;

import com.remare.communicationchannelsauthorizationserver.entity.User;
import com.remare.communicationchannelsauthorizationserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.remare.communicationchannelsauthorizationserver.constant.ExceptionConstant.USER_NOT_FOUND;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByUsername(username);
    User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    return new UserDetailsImpl(user);
  }
}
