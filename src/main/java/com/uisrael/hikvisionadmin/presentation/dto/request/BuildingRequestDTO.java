package com.uisrael.hikvisionadmin.presentation.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuildingRequestDTO {

  @NotBlank
  private String name;

  private String address;

  @NotNull
  @Min(1)
  private Integer numberOfFloors;

  private String description;

  private String phone;

  @NotNull
  private Boolean isActive;

  private LocalDateTime createdDate;

  private String createdUser;

  private String modifiedUser;

  private LocalDateTime modifiedDate;

  private Long adminId;

}
