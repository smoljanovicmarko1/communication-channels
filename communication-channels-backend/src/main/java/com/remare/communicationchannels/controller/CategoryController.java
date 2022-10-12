package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.dto.channel.CategoryDto;
import com.remare.communicationchannels.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("all")
  public List<CategoryDto> findAll() {
    return categoryService.findAll();
  }
}
