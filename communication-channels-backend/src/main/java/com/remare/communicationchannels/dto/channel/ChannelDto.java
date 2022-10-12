package com.remare.communicationchannels.dto.channel;

import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.validator.channel.Channel;
import com.remare.communicationchannels.validator.channel.groups.Edit;
import com.remare.communicationchannels.validator.channel.groups.Save;
import com.remare.communicationchannels.validator.channel.name.ChannelNameNotTaken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Channel(groups = Edit.class)
public class ChannelDto {

  private Long id;

  @ChannelNameNotTaken(groups = Save.class)
  private String name;

  private Date dateCreated;
  private CategoryDto category;
  private ChannelStatusDto channelStatus;
  private CommunicationDirectionDto communicationDirection;
  private List<ChannelShortDto> channels;
  private List<AttachmentDto> attachments;
  private List<UserChannelDto> userChannels;
  private List<PostShortDto> posts;
  private ChannelShortDto parentChannel;
}
