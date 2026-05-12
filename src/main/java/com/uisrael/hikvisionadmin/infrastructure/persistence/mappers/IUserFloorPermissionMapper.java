package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.UserFloorPermission;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserFloorPermissionJpa;

@Mapper(componentModel = "spring")
public interface IUserFloorPermissionMapper {

  @Mapping(target = "user.id", source = "userId")
  @Mapping(target = "floor.id", source = "floorId")
  UserFloorPermissionJpa toEntity(UserFloorPermission domain);

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "floorId", source = "floor.id")
  UserFloorPermission toDomain(UserFloorPermissionJpa entity);
}
