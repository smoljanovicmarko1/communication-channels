package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.entity.User;
import com.remare.communicationchannels.exception.user.UserNotFoundException;
import com.remare.communicationchannels.mapper.UserMapper;
import com.remare.communicationchannels.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @Test
  void findByIdSuccess() {

    User user = new User();
    user.setId(1L);
    user.setFirstName("Mark");
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setFirstName("Mark");
    when(userMapper.toDto(user)).thenReturn(userDto);

    UserDto userDto1 = userService.findById(1L);

    assertEquals(userDto1, userDto);
  }

  @Test
  void findByIdException() {
    when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
    assertThrows(UserNotFoundException.class, () -> userService.findById(anyLong()));
  }
}
