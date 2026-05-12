package com.uisrael.hikvisionadmin.presentation.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.Device;
import com.uisrael.hikvisionadmin.presentation.dto.request.DeviceRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.DeviceResponseDTO;

@Mapper(componentModel = "spring")
public interface IDeviceDtoMapper {
  DeviceResponseDTO toResponseDto(Device device);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "floorId", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdUser", ignore = true)
  Device toDomain(DeviceRequestDTO request);
}
