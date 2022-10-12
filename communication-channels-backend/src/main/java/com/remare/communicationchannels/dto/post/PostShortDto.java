package com.remare.communicationchannels.dto.post;

import com.remare.communicationchannels.dto.Searchable;
import com.remare.communicationchannels.dto.channel.ChannelShortDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostShortDto implements Searchable {

  private Long id;
  private String title;
  private String body;
  private Date dateCreated;
  private UserShortDto user;
  private ChannelShortDto channel;
  private boolean edited;
}
