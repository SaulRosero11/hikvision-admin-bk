package com.uisrael.hikvisionadmin.infrastructure.persistence.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.Admin;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.AdminJpa;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.BuildingJpa;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IAdminJpaMapper {

  private final IBuildingJpaMapper buildingMapper;

  public AdminJpa toEntity(Admin dominio) {
    if (dominio == null)
      return null;

    AdminJpa jpa = AdminJpa.builder()
        .id(dominio.getId())
        .username(dominio.getUsername())
        .password(dominio.getPassword())
        .email(dominio.getEmail())
        .fullName(dominio.getFullName())
        .isActive(dominio.getIsActive())
        .createdDate(dominio.getCreatedDate())
        .lastAccess(dominio.getLastAccess())
        .build();

    if (dominio.getBuildings() != null) {
      List<BuildingJpa> buildingsJpa = dominio.getBuildings().stream()
          .map(buildingMapper::toEntity)
          .peek(b -> b.setAdmin(jpa))
          .collect(Collectors.toList());
      jpa.setBuildings(buildingsJpa);
    }

    return jpa;
  }

  public Admin toDomain(AdminJpa jpa) {
    if (jpa == null)
      return null;

    return Admin.builder()
        .id(jpa.getId())
        .username(jpa.getUsername())
        .password(jpa.getPassword())
        .email(jpa.getEmail())
        .fullName(jpa.getFullName())
        .isActive(jpa.getIsActive())
        .createdDate(jpa.getCreatedDate())
        .lastAccess(jpa.getLastAccess())
        .buildings(jpa.getBuildings() != null ? jpa.getBuildings().stream()
            .map(buildingMapper::toDomain)
            .collect(Collectors.toList()) : new ArrayList<>())
        .build();
  }

}
