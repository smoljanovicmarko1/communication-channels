package com.remare.communicationchannels.mapper;

import com.remare.communicationchannels.dto.post.PostDto;
import com.remare.communicationchannels.dto.post.PostShortDto;
import com.remare.communicationchannels.entity.Post;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {
      UserMapper.class,
      ChannelMapper.class,
      LikeMapper.class,
      AttachmentMapper.class,
      CommentMapper.class
    })
public interface PostMapper {

  PostDto toDto(Post post);

  @InheritInverseConfiguration
  Post toEntity(PostDto postDto);

  List<PostDto> toDtoList(List<Post> list);

  @InheritInverseConfiguration
  List<Post> toEntityList(List<PostDto> list);

  PostShortDto toShortDto(Post post);

  @InheritInverseConfiguration
  Post toShortEntity(PostShortDto postShortDto);

  List<PostShortDto> toShortDtoList(List<Post> list);

  @InheritInverseConfiguration
  List<Post> toShortEntityList(List<PostShortDto> list);
}
