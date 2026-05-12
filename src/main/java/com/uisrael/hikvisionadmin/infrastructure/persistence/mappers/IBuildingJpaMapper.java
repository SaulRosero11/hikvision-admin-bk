package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.Building;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.BuildingJpa;

@Mapper(componentModel = "spring")
public interface IBuildingJpaMapper {

  @Mapping(source = "admin.id", target = "adminId")
  Building toDomain(BuildingJpa entity);

  @Mapping(source = "adminId", target = "admin.id")
  BuildingJpa toEntity(Building building);
}
