package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.dto.Searchable;
import com.remare.communicationchannels.dto.channel.ChannelShortDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.mapper.ChannelMapper;
import com.remare.communicationchannels.mapper.PostMapper;
import com.remare.communicationchannels.mapper.UserMapper;
import com.remare.communicationchannels.repository.ChannelRepository;
import com.remare.communicationchannels.repository.PostRepository;
import com.remare.communicationchannels.repository.UserRepository;
import com.remare.communicationchannels.service.SearchableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchableServiceImpl implements SearchableService {

  private final PostRepository postRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final PostMapper postMapper;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  @Autowired
  public SearchableServiceImpl(
      PostRepository postRepository,
      ChannelRepository channelRepository,
      UserRepository userRepository,
      PostMapper postMapper,
      ChannelMapper channelMapper,
      UserMapper userMapper) {
    this.postRepository = postRepository;
    this.channelRepository = channelRepository;
    this.userRepository = userRepository;
    this.postMapper = postMapper;
    this.channelMapper = channelMapper;
    this.userMapper = userMapper;
  }

  @Override
  public List<Searchable> getChannelsAndPosts(String filterValue, Long userId) {
    List<Searchable> searchableList = new ArrayList<>();
    List<ChannelShortDto> channelShortDtoList =
        channelRepository.findByFilterValue(filterValue, userId).stream()
            .map(channelMapper::toShortDto)
            .collect(Collectors.toList());
    List<PostShortDto> postShortDtoList =
        postRepository.findByFilterValue(filterValue, userId).stream()
            .map(postMapper::toShortDto)
            .collect(Collectors.toList());
    searchableList.addAll(postShortDtoList);
    searchableList.addAll(channelShortDtoList);
    return searchableList;
  }
}
