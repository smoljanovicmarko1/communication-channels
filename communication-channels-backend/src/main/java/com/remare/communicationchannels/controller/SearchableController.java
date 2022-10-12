package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.dto.Searchable;
import com.remare.communicationchannels.service.SearchableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/searchable")
public class SearchableController {

  private final SearchableService searchableService;

  @Autowired
  public SearchableController(SearchableService searchableService) {
    this.searchableService = searchableService;
  }

  @GetMapping("/channels-and-posts")
  public List<Searchable> getChannelsAndPosts(
      @RequestParam("filterValue") String filterValue, @RequestParam("userId") Long userId) {
    return searchableService.getChannelsAndPosts(filterValue, userId);
  }
}
