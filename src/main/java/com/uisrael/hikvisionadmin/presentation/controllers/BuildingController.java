package com.uisrael.hikvisionadmin.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uisrael.hikvisionadmin.application.usecase.entry.IBuildingUseCase;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.infrastructure.security.AdminUserDetails;
import com.uisrael.hikvisionadmin.presentation.dto.request.BuildingRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.BuildingResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/building")
@RequiredArgsConstructor
public class BuildingController {

  private final IBuildingUseCase buildingUseCase;

  @GetMapping
  public ResponseEntity<List<BuildingResponseDTO>> findAll(@RequestParam(required = false) Long adminId) {
    List<BuildingResponseDTO> response;
    if (adminId != null) {
      response = buildingUseCase.findByAdminId(adminId);
    } else {
      response = buildingUseCase.list();
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BuildingResponseDTO> findById(@PathVariable Long id) {
    return buildingUseCase.findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new EntityNotFoundException("Edificio no encontrado con id: " + id));
  }

  @PostMapping
  public ResponseEntity<BuildingResponseDTO> save(@Valid @RequestBody BuildingRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(buildingUseCase.save(request, userDetails.getId(), userDetails.getUsername()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BuildingResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody BuildingRequestDTO request) {

    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.ok(buildingUseCase.update(id, request, userDetails.getUsername()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    buildingUseCase.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  private AdminUserDetails getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof AdminUserDetails)) {
      throw new DomainException("No se encontró una sesión de administrador válida");
    }
    return (AdminUserDetails) auth.getPrincipal();
  }
}
