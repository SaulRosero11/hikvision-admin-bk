package com.uisrael.hikvisionadmin.presentation.mappers;

import org.mapstruct.Mapper;

import com.uisrael.hikvisionadmin.domain.entities.Admin;
import com.uisrael.hikvisionadmin.presentation.dto.request.AdminRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.AdminResponseDTO;

@Mapper(componentModel = "spring")
public interface IAuthDtoMapper {

  Admin toDomain(AdminRequestDTO dto);

  AdminResponseDTO toResponseDTO(Admin admin);

}
