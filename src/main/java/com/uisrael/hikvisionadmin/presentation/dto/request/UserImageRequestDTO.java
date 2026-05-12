package com.uisrael.hikvisionadmin.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserImageRequestDTO {

  @NotBlank(message = "Image Base64 content is required")
  private String imageBase64;

  @NotBlank(message = "Image type (e.g., 'face') is required")
  private String imageType;

  private String extension;
}
