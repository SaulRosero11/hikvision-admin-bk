package com.uisrael.hikvisionadmin.presentation.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.AccessValidity;
import com.uisrael.hikvisionadmin.domain.entities.User;
import com.uisrael.hikvisionadmin.domain.entities.UserFloorPermission;
import com.uisrael.hikvisionadmin.domain.entities.UserImage;
import com.uisrael.hikvisionadmin.presentation.dto.request.AccessValidityRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.request.UserImageRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.request.UserRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.AccessValidityResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.UserFloorPermissionResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.UserImageResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface IUserDtoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "floorPermissions", ignore = true)
  @Mapping(target = "isActive", ignore = true)
  @Mapping(target = "createdUser", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedUser", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  User toDomain(UserRequestDTO dto);

  @Mapping(target = "buildingName", ignore = true)
  UserResponseDTO toResponseDto(User user);

  AccessValidity toDomain(AccessValidityRequestDTO dto);

  AccessValidityResponseDTO toResponseDto(AccessValidity domain);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", ignore = true)
  UserImage toDomain(UserImageRequestDTO dto);

  UserImageResponseDTO toResponseDto(UserImage domain);

  List<UserImage> toUserImageDomainList(List<UserImageRequestDTO> dtos);

  List<UserImageResponseDTO> toUserImageResponseDtoList(List<UserImage> domainList);

  @Mapping(target = "floorName", ignore = true)
  UserFloorPermissionResponseDTO toResponseDto(UserFloorPermission domain);

  List<UserFloorPermissionResponseDTO> toPermissionResponseDtoList(List<UserFloorPermission> domainList);
}
