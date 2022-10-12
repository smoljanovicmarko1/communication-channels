package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.AccountVerificationRequest;
import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

  UserDto findById(Long id);

  List<UserDto> findAll();

  UserDto save(@Validated UserDto userDto);

  UserDto deleteById(Long id);

  UserDto findByUsername(String username);

  UserDto findByEmail(String email);

  UserDto add(UserDto userDto);

  Page<UserShortDto> findAllPagination(Pageable pageable);

  ByteArrayResource findProfilePictureById(Long id) throws IOException;

  String uploadProfileImage(Long id, MultipartFile profileImage) throws IOException;

  void saveDefaultProfileImage(Long id) throws IOException;

  Page<UserShortDto> findAllPaginationUsersInChannel(
      Long channelId, Long loggedUserId, Pageable pageable);

  Page<UserShortDto> findAllByUserNotChannel(Long channelId, Long loggedUserId, Pageable pageable);

  boolean verifyActivationToken(AccountVerificationRequest accountVerificationRequest)
      throws NoSuchAlgorithmException;

  void deleteActivationToken(AccountVerificationRequest accountVerificationRequest);
}
