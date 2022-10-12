package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.dto.channel.CommunicationDirectionDto;
import com.remare.communicationchannels.mapper.ChannelMapper;
import com.remare.communicationchannels.repository.CommunicationDirectionRepository;
import com.remare.communicationchannels.service.CommunicationDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationDirectionServiceImpl implements CommunicationDirectionService {

  private final ChannelMapper channelMapper;
  private final CommunicationDirectionRepository communicationDirectionRepository;

  @Autowired
  public CommunicationDirectionServiceImpl(
      ChannelMapper channelMapper,
      CommunicationDirectionRepository communicationDirectionRepository) {
    this.channelMapper = channelMapper;
    this.communicationDirectionRepository = communicationDirectionRepository;
  }

  @Override
  public List<CommunicationDirectionDto> findAll() {
    return channelMapper.toCommunicationDirectionDtoList(
        communicationDirectionRepository.findAll());
  }
}
