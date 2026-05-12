package com.uisrael.hikvisionadmin.presentation.dto.request;

import java.util.List;

import com.uisrael.hikvisionadmin.domain.enums.Gender;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {

  @NotBlank(message = "Identification is required")
  private String identification;

  @NotBlank(message = "Full name is required")
  private String fullName;

  @NotNull(message = "Building ID is required")
  private Long buildingId;

  private Gender gender;
  private Integer roomNumber;
  private Integer floorNumber;
  private String doorRight;
  private Boolean localUiRight;

  private Boolean isActive;

  @Valid
  private AccessValidityRequestDTO validity;

  @Valid
  private List<UserImageRequestDTO> images;

  @NotNull(message = "At least one floor must be assigned or an empty list provided")
  private List<Long> floorIds;
}
