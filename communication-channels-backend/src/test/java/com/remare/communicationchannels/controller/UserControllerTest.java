package com.remare.communicationchannels.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  void findByIdSuccess() throws Exception {

    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setFirstName("John");

    when(userService.findById(anyLong())).thenReturn(userDto);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/1").accept(APPLICATION_JSON);

    MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    String expected = objectMapper.writeValueAsString(userDto);
    String actual = mvcResult.getResponse().getContentAsString();

    assertThat(actual).isEqualTo(expected);
  }

  // TODO findByIdException

}
