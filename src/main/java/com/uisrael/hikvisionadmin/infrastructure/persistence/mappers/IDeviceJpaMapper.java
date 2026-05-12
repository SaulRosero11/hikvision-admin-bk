package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.Device;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.DeviceJpa;

@Mapper(componentModel = "spring")
public interface IDeviceJpaMapper {

  @Mapping(source = "floor.id", target = "floorId")
  Device toDomain(DeviceJpa entity);

  @Mapping(source = "floorId", target = "floor.id")
  DeviceJpa toEntity(Device domain);
}
