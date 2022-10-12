package com.remare.communicationchannels.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {

  private Long id;
  private String name;
  private List<UserPermissionDto> userPermissions;
}
