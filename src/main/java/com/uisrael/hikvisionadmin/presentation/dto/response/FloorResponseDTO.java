package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FloorResponseDTO {
  private Long id;
  private Integer floorNumber;
  private String name;
  private String description;
  private Long buildingId;
  private Boolean isActive;

  private String createdUser;
  private LocalDateTime createdDate;
  private String modifiedUser;
  private LocalDateTime modifiedDate;
}
