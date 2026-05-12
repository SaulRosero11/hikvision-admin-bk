package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Admin {

  private final Long id;
  private final String username;
  private final String password;
  private final String email;
  private final String fullName;
  private final LocalDateTime lastAccess;
  private final Boolean isActive;
  private final LocalDateTime createdDate;
  private final LocalDateTime modifiedDate;
  private final String modifiedUser;
  private final List<Building> buildings;

  public void validate() {
    if (username == null || username.isBlank()) {
      throw new DomainException("El nombre de usuario es obligatorio");
    }
    if (username.length() < 4 || username.length() > 50) {
      throw new DomainException("EL nombre del usuario debe tener entre 4 y 50 caracteres");
    }
    if (password == null || password.isBlank()) {
      throw new DomainException("La contraseña es obligatoria");
    }
    if (email == null || email.isBlank()) {
      throw new DomainException("El correo es obligatorio");
    }
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new DomainException("El formato del correo no es válido");
    }
    if (buildings == null || buildings.isEmpty()) {
      throw new DomainException("El administrador debe tener al menos un edificio asociado");
    }
  }

  public List<Building> getBuildings() {
    return buildings != null ? buildings : Collections.emptyList();
  }

}
