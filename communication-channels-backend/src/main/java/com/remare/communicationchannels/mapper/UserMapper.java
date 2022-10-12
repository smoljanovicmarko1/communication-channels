package com.remare.communicationchannels.mapper;

import com.remare.communicationchannels.dto.user.UserDto;
import com.remare.communicationchannels.dto.user.UserPermissionDto;
import com.remare.communicationchannels.dto.user.UserRoleDto;
import com.remare.communicationchannels.dto.user.UserShortDto;
import com.remare.communicationchannels.entity.User;
import com.remare.communicationchannels.entity.UserPermission;
import com.remare.communicationchannels.entity.UserRole;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {PostMapper.class})
public interface UserMapper {

  UserDto toDto(User user);

  @InheritInverseConfiguration
  User toEntity(UserDto userDto);

  List<UserDto> toDtoList(List<User> list);

  @InheritInverseConfiguration
  List<User> toEntityList(List<UserDto> list);

  UserRoleDto toUserRoleDto(UserRole userRole);

  @InheritInverseConfiguration
  UserRole toUserRoleEntity(UserRoleDto userRoleDto);

  UserPermissionDto toUserPermissionDto(UserPermission userPermission);

  @InheritInverseConfiguration
  UserPermission toUserPermissionEntity(UserPermissionDto userPermissionDto);

  List<UserPermissionDto> toUserPermissionDtoList(List<UserPermission> list);

  @InheritInverseConfiguration
  List<UserPermission> toUserPermissionEntityList(List<UserPermissionDto> list);

  UserShortDto toShortDto(User user);

  @InheritInverseConfiguration
  User toShortEntity(UserShortDto user);

  List<UserShortDto> toShortDtoList(List<User> list);

  @InheritInverseConfiguration
  List<User> toShortEntityList(List<UserShortDto> list);
}
