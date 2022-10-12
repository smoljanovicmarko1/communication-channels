package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.constant.ExceptionConstant;
import com.remare.communicationchannels.constant.FileConstant;
import com.remare.communicationchannels.dto.AccountVerificationRequest;
import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import com.remare.communicationchannels.entity.AccountActivationToken;
import com.remare.communicationchannels.entity.User;
import com.remare.communicationchannels.exception.user.InvalidAccountActivationTokenException;
import com.remare.communicationchannels.exception.user.UserNotFoundException;
import com.remare.communicationchannels.mapper.UserMapper;
import com.remare.communicationchannels.repository.AccountActivationTokenRepository;
import com.remare.communicationchannels.repository.UserChannelRepository;
import com.remare.communicationchannels.repository.UserRepository;
import com.remare.communicationchannels.repository.UserRoleRepository;
import com.remare.communicationchannels.service.UserService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UserRoleRepository userRoleRepository;
  private final UserChannelRepository userChannelRepository;
  private final AccountActivationTokenRepository accountActivationTokenRepository;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository,
      UserMapper userMapper,
      PasswordEncoder passwordEncoder,
      UserRoleRepository userRoleRepository,
      UserChannelRepository userChannelRepository,
      AccountActivationTokenRepository accountActivationTokenRepository) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.userRoleRepository = userRoleRepository;
    this.userChannelRepository = userChannelRepository;
    this.accountActivationTokenRepository = accountActivationTokenRepository;
  }

  @Override
  public UserDto findById(Long id) {
    return userMapper.toDto(
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(ExceptionConstant.USER_NOT_FOUND_BY_ID, id))));
  }

  @Override
  public List<UserDto> findAll() {
    return userMapper.toDtoList(userRepository.findAll());
  }

  @Override
  public UserDto save(@Validated UserDto userDto) {
    User user = userMapper.toEntity(userDto);
    user.setProfilePictureUrl(
        userRepository
            .findById(userDto.getId())
            .orElseThrow(
                () -> new UserNotFoundException("User with id " + userDto.getId() + " not found"))
            .getProfilePictureUrl());
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public UserDto deleteById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(ExceptionConstant.USER_NOT_FOUND_BY_ID, id)));
    userRepository.deleteById(id);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto findByUsername(String username) {
    return userMapper.toDto(
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(ExceptionConstant.USER_NOT_FOUND_BY_USERNAME, username))));
  }

  @Override
  public UserDto findByEmail(String email) {
    return userMapper.toDto(
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(ExceptionConstant.USER_NOT_FOUND_BY_EMAIL, email))));
  }

  @Override
  public UserDto add(UserDto userDto) {
    User user = userMapper.toEntity(userDto);
    user.setRole(
        userRoleRepository
            .findByName(user.getRole().getName())
            .orElseThrow(() -> new RuntimeException("Role Not Found!")));
    user.setUsername(user.getEmail());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setFirstName("First_name");
    user.setLastName("Last_name");
    user.setDateCreated(new Date());
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setEnabled(true);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public Page<UserShortDto> findAllPagination(Pageable pageable) {
    return userRepository.findAll(pageable).map(userMapper::toShortDto);
  }

  @Override
  public ByteArrayResource findProfilePictureById(Long id) throws IOException {
    String pictureUrl =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."))
            .getProfilePictureUrl();

    File file = new File(FileConstant.ASSETS_FOLDER + pictureUrl);

    if (!file.exists() || !file.isFile()) {
      throw new FileNotFoundException("The requested file not found");
    }
    Path path = Paths.get(file.getAbsolutePath());

    return new ByteArrayResource(Files.readAllBytes(path));
  }

  @Override
  public String uploadProfileImage(Long id, MultipartFile profileImage) throws IOException {

    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

    System.out.println(System.getProperty("user.dir"));

    Path folder =
        Paths.get(
            FileConstant.ASSETS_FOLDER
                + FileConstant.USER_FOLDER
                + File.separator
                + id
                + File.separator
                + FileConstant.PROFILE_FOLDER_NAME);

    if (!Files.exists(folder)) {
      Files.createDirectories(folder);
    } else {
      File file = folder.toFile();
      FileUtils.cleanDirectory(file);
    }

    user.setProfilePictureUrl(
        FileConstant.USER_FOLDER
            + id
            + File.separator
            + FileConstant.PROFILE_FOLDER_NAME
            + File.separator
            + profileImage.getOriginalFilename());

    Files.copy(profileImage.getInputStream(), folder.resolve(profileImage.getOriginalFilename()));

    userRepository.save(user);

    return "Profile picture is saved";
  }

  @Override
  public void saveDefaultProfileImage(Long id) throws IOException {

    Path folder =
        Paths.get(
            FileConstant.ASSETS_FOLDER
                + FileConstant.USER_FOLDER
                + File.separator
                + id
                + File.separator
                + FileConstant.PROFILE_FOLDER_NAME);

    File defaultProfileImage =
        new File(FileConstant.ASSETS_FOLDER + FileConstant.DEFAULT_PROFILE_IMAGE_PATH);

    if (!Files.exists(folder)) {
      Files.createDirectories(folder);
    } else {
      File file = folder.toFile();
      FileUtils.cleanDirectory(file);
    }

    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

    user.setProfilePictureUrl(
        FileConstant.USER_FOLDER
            + id
            + File.separator
            + FileConstant.PROFILE_FOLDER_NAME
            + File.separator
            + defaultProfileImage.getName());

    Files.copy(
        new FileInputStream(defaultProfileImage), folder.resolve(defaultProfileImage.getName()));

    userRepository.save(user);
  }

  @Override
  public Page<UserShortDto> findAllPaginationUsersInChannel(
      Long channelId, Long loggedUserId, Pageable pageable) {
    return userChannelRepository
        .findAllByUserInChannel(channelId, loggedUserId, pageable)
        .map(userChannel -> userMapper.toShortDto(userChannel.getUser()));
  }

  @Override
  public Page<UserShortDto> findAllByUserNotChannel(
      Long channelId, Long loggedUserId, Pageable pageable) {
    return userRepository
        .findAllNotInChannel(channelId, loggedUserId, pageable)
        .map(userMapper::toShortDto);
  }

  @Override
  public boolean verifyActivationToken(AccountVerificationRequest accountVerificationRequest)
      throws NoSuchAlgorithmException {
    Long id = accountVerificationRequest.getUserId();
    String token = accountVerificationRequest.getToken();

    AccountActivationToken accountActivationToken =
        accountActivationTokenRepository
            .findByUserIdAndToken(id, token)
            .orElseThrow(
                () ->
                    new InvalidAccountActivationTokenException(
                        "The passed token is used or does not exist."));

    return true;
  }

  @Override
  public void deleteActivationToken(AccountVerificationRequest accountVerificationRequest) {
    Long id = accountVerificationRequest.getUserId();
    String token = accountVerificationRequest.getToken();

    AccountActivationToken accountActivationToken =
        accountActivationTokenRepository
            .findByUserIdAndToken(id, token)
            .orElseThrow(
                () ->
                    new InvalidAccountActivationTokenException(
                        "The passed token is used or does not exist."));

    accountActivationTokenRepository.delete(accountActivationToken);
  }
}
