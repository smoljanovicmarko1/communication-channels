package com.remare.communicationchannels.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remare.communicationchannels.dto.comment.CommentDto;
import com.remare.communicationchannels.service.CommentService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

  @Autowired protected MockMvc mvc;

  @Autowired WebApplicationContext webApplicationContext;

  @Autowired ObjectMapper objectMapper;

  @MockBean CommentService commentService;

  void findById() throws Exception {

    CommentDto commentDto = new CommentDto();
    commentDto.setId(1L);
    commentDto.setText("Peter");

    Mockito.when(commentService.findById(Mockito.anyLong())).thenReturn(commentDto);

    RequestBuilder request =
        MockMvcRequestBuilders.get("/comment/1").accept(MediaType.APPLICATION_JSON);

    MvcResult result =
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String actual = result.getResponse().getContentAsString();
    String expected = objectMapper.writeValueAsString(commentDto);

    assertEquals(actual, expected);
  }
}
