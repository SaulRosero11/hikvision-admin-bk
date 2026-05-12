package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.AccessValidity;
import com.uisrael.hikvisionadmin.domain.entities.User;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.AccessValidityEmbeddable;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserJpa;

@Mapper(componentModel = "spring", uses = { IUserImageMapper.class, IUserFloorPermissionMapper.class })
public interface IUserJpaMapper {

  @Mapping(target = "validity", source = "validity")
  UserJpa toEntity(User domain);

  @Mapping(target = "validity", source = "validity")
  User toDomain(UserJpa entity);

  // Mapeo para el Value Object / Embeddable
  AccessValidityEmbeddable toEmbeddable(AccessValidity domain);

  AccessValidity toValueObject(AccessValidityEmbeddable entity);
}
