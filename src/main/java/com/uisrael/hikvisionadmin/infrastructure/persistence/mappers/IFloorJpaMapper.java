package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.Floor;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.FloorJpa;

@Mapper(componentModel = "spring")
public interface IFloorJpaMapper {

  @Mapping(source = "building.id", target = "buildingId")
  Floor toDomain(FloorJpa entity);

  @Mapping(source = "buildingId", target = "building.id")
  FloorJpa toEntity(Floor domain);
}
