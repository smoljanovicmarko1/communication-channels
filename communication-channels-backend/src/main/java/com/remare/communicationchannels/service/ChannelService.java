package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.channel.ChannelDto;
import com.remare.communicationchannels.dto.channel.ChannelShortDto;
import com.remare.communicationchannels.dto.channel.UserChannelDto;
import com.remare.communicationchannels.entity.UserChannel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChannelService {

  List<ChannelShortDto> findAll();

  ChannelDto findById(Long id);

  ChannelDto save(ChannelDto channelDto);

  List<ChannelShortDto> findAllByUserId(Long id);

  ByteArrayResource findProfilePictureById(Long id) throws IOException;

  String uploadProfileImage(Long id, MultipartFile profileImage) throws IOException;

  ChannelDto findByName(String name);

  Long findIdByPostId(Long postId);

  boolean isUserInChannel(Long userId, Long channelId);

  List<ChannelShortDto> findAllByChannelAndUser(Long channelId, Long userId);

  UserChannelDto saveUserChannel(UserChannel userChannel);

  UserChannelDto deleteUserChannel(Long userId, Long channelId);

  ChannelShortDto deleteById(Long id);
}
