package com.uisrael.hikvisionadmin.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingResponseDTO {

  private String name;
  private String address;
  private Integer numberOfFloors;
  private String description;
  private String phone;
  private Boolean isActive;

}
