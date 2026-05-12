package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.UserImage;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserImageJpa;

@Mapper(componentModel = "spring")
public interface IUserImageMapper {

  @Mapping(target = "user.id", source = "userId")
  UserImageJpa toEntity(UserImage domain);

  @Mapping(target = "userId", source = "user.id")
  UserImage toDomain(UserImageJpa entity);
}
