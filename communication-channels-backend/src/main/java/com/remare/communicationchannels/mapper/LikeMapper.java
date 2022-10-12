package com.remare.communicationchannels.mapper;

import com.remare.communicationchannels.dto.like.LikeDto;
import com.remare.communicationchannels.dto.like.LikeStatusDto;
import com.remare.communicationchannels.entity.Like;
import com.remare.communicationchannels.entity.LikeStatus;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class, PostMapper.class, CommentMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface LikeMapper {

  LikeDto toDto(Like like);

  @InheritInverseConfiguration
  Like toEntity(LikeDto likeDto);

  List<LikeDto> toDtoList(List<Like> list);

  @InheritInverseConfiguration
  List<Like> toEntityList(List<LikeDto> list);

  LikeStatusDto toStatusDto(LikeStatus likeStatus);

  @InheritInverseConfiguration
  LikeStatus toStatusEntity(LikeStatusDto likeStatusDto);

  List<LikeStatusDto> toStatusDtoList(List<LikeStatus> list);

  @InheritInverseConfiguration
  List<LikeStatus> toStatusEntityList(List<LikeStatusDto> list);
}
