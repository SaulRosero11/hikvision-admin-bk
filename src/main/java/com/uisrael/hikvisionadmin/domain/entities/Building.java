package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Building {

  private final Long id;
  private final String name;
  private final String address;
  private final Integer numberOfFloors;
  private final String description;
  private final String phone;
  private final Boolean isActive;
  private final String createdUser;
  private final LocalDateTime createdDate;
  private final String modifiedUser;
  private final LocalDateTime modifiedDate;
  private final Long adminId;

  public void validate() {
    if (name == null || name.trim().isEmpty()) {
      throw new DomainException("El nombre del edificio es obligatorio");
    }
    if (numberOfFloors == null || numberOfFloors < 1) {
      throw new DomainException("El numero de pisos debe ser mayor a 0.");
    }
  }

}
