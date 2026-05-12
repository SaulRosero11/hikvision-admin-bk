package com.uisrael.hikvisionadmin.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FloorRequestDTO {

  @NotNull(message = "Floor number is required")
  @Min(0)
  private Integer floorNumber;

  @NotBlank(message = "Floor name is required")
  private String name;

  private String description;

  private Boolean isActive;
}
