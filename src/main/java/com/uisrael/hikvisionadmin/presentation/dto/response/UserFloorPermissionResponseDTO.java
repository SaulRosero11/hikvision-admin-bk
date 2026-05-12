package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserFloorPermissionResponseDTO {
  private Long id;
  private Long floorId;
  private String floorName;
  private Boolean accessAllowed;
  private Boolean isSynced;

  private LocalDateTime assignedDate;
  private LocalDateTime expirationDate;
  private Boolean isActive;
}
