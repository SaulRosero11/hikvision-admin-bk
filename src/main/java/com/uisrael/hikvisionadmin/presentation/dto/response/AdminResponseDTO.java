package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDTO {

  private String token;
  private String tipo;
  private String username;
  private String email;
  private String fullName;
  private LocalDateTime lastAccess;
  private Boolean isActive;

  private Long expiracion;

  private java.util.List<BuildingResponseDTO> buildings;

  @Data
  @Builder
  public static class BuildingResponseDTO {
    private Long id;
    private String name;
    private String address;
  }

}
