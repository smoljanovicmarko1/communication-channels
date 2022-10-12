package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.channel.CategoryDto;

import java.util.List;

public interface CategoryService {

  List<CategoryDto> findAll();
}
