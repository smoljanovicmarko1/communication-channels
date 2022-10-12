package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.channel.CommunicationDirectionDto;

import java.util.List;

public interface CommunicationDirectionService {
  List<CommunicationDirectionDto> findAll();
}
