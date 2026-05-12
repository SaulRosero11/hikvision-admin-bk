package com.uisrael.hikvisionadmin.presentation.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uisrael.hikvisionadmin.domain.entities.Floor;
import com.uisrael.hikvisionadmin.presentation.dto.request.FloorRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.FloorResponseDTO;

@Mapper(componentModel = "spring")
public interface IFloorDtoMapper {

  FloorResponseDTO toResponseDto(Floor floor);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdUser", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "modifiedUser", ignore = true)
  Floor toDomain(FloorRequestDTO request);
}
