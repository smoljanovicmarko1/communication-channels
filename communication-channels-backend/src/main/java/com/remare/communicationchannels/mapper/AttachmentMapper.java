package com.remare.communicationchannels.mapper;

import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.entity.Attachment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class, LikeMapper.class})
public interface AttachmentMapper {
  AttachmentDto toDto(Attachment attachment);

  @InheritInverseConfiguration
  Attachment toEntity(AttachmentDto attachmentDto);

  List<AttachmentDto> toDtoList(List<Attachment> list);

  @InheritInverseConfiguration
  List<Attachment> toEntityList(List<AttachmentDto> list);
}
