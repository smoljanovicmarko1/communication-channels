package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.dto.channel.CommunicationDirectionDto;
import com.remare.communicationchannels.service.CommunicationDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("communication-direction")
public class CommunicationDirectionController {

  private final CommunicationDirectionService communicationDirectionService;

  @Autowired
  public CommunicationDirectionController(
      CommunicationDirectionService communicationDirectionService) {
    this.communicationDirectionService = communicationDirectionService;
  }

  @GetMapping("all")
  public List<CommunicationDirectionDto> findAll() {
    return communicationDirectionService.findAll();
  }
}
