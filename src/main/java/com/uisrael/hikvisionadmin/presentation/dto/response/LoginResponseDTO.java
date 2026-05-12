package com.uisrael.hikvisionadmin.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

  private String token;
  private String tipo;
  private Long administradorId;
  private String username;
  private String email;
  private String nombreCompleto;
  private java.util.List<AdminResponseDTO.BuildingResponseDTO> buildings;

  private Long expiracion;

}
