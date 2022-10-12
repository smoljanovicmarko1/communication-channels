package com.remare.communicationchannels.mapper;

import com.remare.communicationchannels.dto.comment.CommentDto;
import com.remare.communicationchannels.dto.comment.CommentShortDto;
import com.remare.communicationchannels.dto.comment.CommentStatusDto;
import com.remare.communicationchannels.entity.Comment;
import com.remare.communicationchannels.entity.CommentStatus;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {
      UserMapper.class,
      LikeMapper.class,
      PostMapper.class,
      AttachmentMapper.class,
      ChannelMapper.class
    })
public interface CommentMapper {

  CommentDto toDto(Comment comment);

  @InheritInverseConfiguration
  Comment toEntity(CommentDto commentDto);

  List<CommentDto> toDtoList(List<Comment> list);

  @InheritInverseConfiguration
  List<Comment> toEntityList(List<CommentDto> list);

  CommentShortDto toShortDto(Comment comment);

  @InheritInverseConfiguration
  Comment toShortEntity(CommentShortDto commentDto);

  List<CommentShortDto> toShortDtoList(List<Comment> list);

  @InheritInverseConfiguration
  List<Comment> toShortEntityList(List<CommentShortDto> list);

  // CommentStatus:
  CommentStatusDto toCommentStatusDto(CommentStatus commentStatus);

  @InheritInverseConfiguration
  CommentStatus toCommentStatusEntity(CommentStatusDto commentStatusDto);

  //    LikeStatusDto toLikeStatusDto(LikeStatus likeStatus);
  //
  //    @InheritInverseConfiguration
  //    LikeStatus toLikeStatusEntity(LikeStatusDto likeStatus);
}
