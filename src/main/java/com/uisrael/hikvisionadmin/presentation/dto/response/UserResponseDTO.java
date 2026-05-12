package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.uisrael.hikvisionadmin.domain.enums.Gender;

import lombok.Data;

@Data
public class UserResponseDTO {

  private Long id;
  private String identification;
  private String fullName;
  private LocalDateTime createdDate;

  private Long buildingId;
  private String buildingName;

  // Hikvision fields
  private Gender gender;
  private Integer roomNumber;
  private Integer floorNumber;
  private String doorRight;
  private Boolean localUiRight;

  private AccessValidityResponseDTO validity;
  private List<UserImageResponseDTO> images;

  // Lista detallada de permisos para ver el estado de sincronización en el Front
  private List<UserFloorPermissionResponseDTO> floorPermissions;
}
