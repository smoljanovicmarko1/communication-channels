package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.dto.post.PostDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PutMapping
  public PostDto update(@RequestBody PostDto postDto) {
    return postService.update(postDto);
  }

  @DeleteMapping("{id}")
  public PostShortDto deleteById(@PathVariable Long id) {
    return postService.deleteById(id);
  }

  @GetMapping("/all")
  public List<PostDto> findAll() {
    return postService.findAll();
  }

  @GetMapping("/{id}")
  public PostDto findById(@PathVariable("id") Long id) {
    return postService.findById(id);
  }

  // todo:videti kako da se imenuje kasnije
  @PostMapping("/addComment")
  public PostDto addComment(@RequestBody PostDto postDto) {
    return postService.update(postDto);
  }

  @PostMapping("/save")
  public PostDto save(@RequestBody PostDto postDto) {
    return postService.save(postDto);
  }

  @PostMapping("/addLike")
  public PostDto addLike(@RequestBody PostDto postDto) {
    return this.postService.save(postDto);
  }
}
