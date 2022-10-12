package com.remare.communicationchannels.service;

import com.remare.communicationchannels.dto.Searchable;

import java.util.List;

public interface SearchableService {
  List<Searchable> getChannelsAndPosts(String filterValue, Long userId);
}
