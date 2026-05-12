package com.uisrael.hikvisionadmin.presentation.dto.request;

import jakarta.validation.constraints.Email;
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
public class AdminRequestDTO {

  @NotBlank(message = "El username es obligatorio")
  @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
  private String username;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
  private String password;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  private String email;

  private String fullName;

  private Long buildingId;
}
