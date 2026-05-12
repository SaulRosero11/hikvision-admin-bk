package com.uisrael.hikvisionadmin.presentation.mappers;

import org.mapstruct.Mapper;

import com.uisrael.hikvisionadmin.domain.entities.Building;
import com.uisrael.hikvisionadmin.presentation.dto.request.BuildingRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.BuildingResponseDTO;

@Mapper(componentModel = "spring")
public interface IBuildingDtoMapper {

  Building toDomain(BuildingRequestDTO dto);

  BuildingResponseDTO toResponseDto(Building building);

}
