package com.uisrael.hikvisionadmin.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {

  @NotBlank(message = "La contraseña actual es obligatoria")
  private String passwordActual;

  @NotBlank(message = "La nueva contraseña es obligatoria")
  @Size(min = 6, message = "La nueva contraseña debe tener mínimo 6 caracteres")
  private String passwordNuevo;

}
