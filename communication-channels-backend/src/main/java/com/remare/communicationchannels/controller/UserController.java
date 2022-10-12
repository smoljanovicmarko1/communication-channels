package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.dto.AccountVerificationRequest;
import com.remare.communicationchannels.dto.MakeAccountRequestDto;
import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import com.remare.communicationchannels.http.HttpResponse;
import com.remare.communicationchannels.service.EmailService;
import com.remare.communicationchannels.service.UserService;
import com.remare.communicationchannels.util.ValidatorWrapper;
import com.remare.communicationchannels.validator.user.groups.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final EmailService emailService;

  @Autowired
  public UserController(UserService userService, EmailService emailService) {
    this.userService = userService;
    this.emailService = emailService;
  }

  @GetMapping("find-by-username")
  public UserDto findByUsername(@RequestParam("username") String username) {
    return userService.findByUsername(username);
  }

  @GetMapping("/{id}")
  public UserDto findById(@PathVariable("id") @Validated Long id) {
    return userService.findById(id);
  }

  @PostMapping("/save")
  public UserDto save(@Validated @RequestBody UserDto userDto) {
    return userService.save(userDto);
  }

  @GetMapping("/all")
  public List<UserDto> findAll() {
    return userService.findAll();
  }

  @PostMapping("/add")
  public UserDto add(@Validated(Add.class) @RequestBody UserDto userDto)
      throws IOException, NoSuchAlgorithmException, MessagingException {

    String password = generateRandomPassword();
    userDto.setPassword(password); // dummy password

    UserDto savedUserDto = userService.add(userDto);

    userService.saveDefaultProfileImage(savedUserDto.getId());

    //        emailService.sendPasswordToUserEmail(email, password);

    emailService.sendAccountActivationLinkToUserEmail(savedUserDto);

    return savedUserDto;
  }

  @GetMapping("all-pagination")
  public Page<UserShortDto> findAllPagination(Pageable pageable) {
    return userService.findAllPagination(pageable);
  }

  @GetMapping("{id}/profile-picture")
  public ResponseEntity<Resource> findProfilePictureById(@PathVariable Long id) throws IOException {

    ByteArrayResource resource = userService.findProfilePictureById(id);

    return ResponseEntity.ok()
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  @PostMapping("/upload-profile-image")
  public ResponseEntity<HttpResponse> uploadProfileImage(
      @RequestParam MultipartFile profileImage, @RequestParam Long id) throws IOException {

    String resultMessage = userService.uploadProfileImage(id, profileImage);

    return ResponseEntity.ok()
        .body(new HttpResponse(200, HttpStatus.OK, "PROFILE_IMAGE_CHANGED", resultMessage));
  }

  private String generateRandomPassword() {
    int leftLimit = 33;
    int rightLimit = 122;
    int targetStringLength = 10;
    Random random = new SecureRandom();

    String generatedString =
        random
            .ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

    return generatedString;
  }

  @GetMapping("all-pagination-users-in-channel")
  public Page<UserShortDto> findAllPaginationUsersInChannel(
      @RequestParam Long channelId,
      @RequestParam Long loggedUserId,
      @RequestParam Integer page,
      @RequestParam Integer size) {
    return userService.findAllPaginationUsersInChannel(
        channelId, loggedUserId, PageRequest.of(page, size));
  }

  @GetMapping("all-pagination-users-not-in-channel")
  public Page<UserShortDto> findAllByUserNotChannel(
      @RequestParam Long channelId,
      @RequestParam Long loggedUserId,
      @RequestParam Integer page,
      @RequestParam Integer size) {
    return userService.findAllByUserNotChannel(channelId, loggedUserId, PageRequest.of(page, size));
  }

  @PostMapping("verify-activation-token")
  public ResponseEntity<UserDto> findProfilePictureById(
      @RequestBody AccountVerificationRequest accountVerificationRequest)
      throws IOException, NoSuchAlgorithmException {

    userService.verifyActivationToken(accountVerificationRequest);
    UserDto result = userService.findById(accountVerificationRequest.getUserId());

    return ResponseEntity.ok().body(result);
  }

  @PostMapping("make-account")
  public ResponseEntity<UserDto> makeAccount(@RequestBody MakeAccountRequestDto makeAccountRequest)
      throws IOException, NoSuchAlgorithmException {

    AccountVerificationRequest accountVerificationRequest = new AccountVerificationRequest();
    accountVerificationRequest.setToken(makeAccountRequest.getToken());
    accountVerificationRequest.setUserId(makeAccountRequest.getUser().getId());

    userService.verifyActivationToken(accountVerificationRequest);

    UserDto savedUser = userService.save(makeAccountRequest.getUser());

    userService.deleteActivationToken(accountVerificationRequest);

    return ResponseEntity.ok().body(savedUser);
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class
  }) // TODO not working inside @RestControllerAdvice
  public ResponseEntity<Object> validationError(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());

    List<ValidatorWrapper> validatorWrappers = new ArrayList<>();
    ex.getBindingResult()
        .getGlobalErrors()
        .forEach(
            objectError ->
                validatorWrappers.add(
                    new ValidatorWrapper("global", objectError.getDefaultMessage())));

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError ->
                validatorWrappers.add(
                    new ValidatorWrapper(fieldError.getField(), fieldError.getDefaultMessage())));

    body.put("error", validatorWrappers);

    return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }
}
