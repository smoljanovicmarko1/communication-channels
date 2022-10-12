package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.dto.channel.CategoryDto;
import com.remare.communicationchannels.mapper.ChannelMapper;
import com.remare.communicationchannels.repository.CategoryRepository;
import com.remare.communicationchannels.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final ChannelMapper channelMapper;
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryServiceImpl(ChannelMapper channelMapper, CategoryRepository categoryRepository) {
    this.channelMapper = channelMapper;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<CategoryDto> findAll() {
    return channelMapper.toCategoryDtoList(categoryRepository.findAll());
  }
}
