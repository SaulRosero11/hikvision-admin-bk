package com.uisrael.hikvisionadmin.domain.entities;

import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserImage {
  private final Long id;
  private final String imageBase64;
  private final String imageType;
  private final String extension;
  private final Long userId;

  public void validate() {
    if (imageBase64 == null || imageBase64.trim().isEmpty()) {
      throw new DomainException("The image Base64 data is required.");
    }

    if (extension != null && !extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("jpeg")) {
      // Esto es preventivo, muchos lectores Hikvision solo aceptan JPG
    }
  }
}
