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

import com.uisrael.hikvisionadmin.application.usecase.entry.IFloorUseCase;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.infrastructure.security.AdminUserDetails;
import com.uisrael.hikvisionadmin.presentation.dto.request.FloorRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.FloorResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/floors")
@RequiredArgsConstructor
public class FloorController {

  private final IFloorUseCase floorUseCase;

  @GetMapping
  public ResponseEntity<List<FloorResponseDTO>> findAll(@RequestParam(required = false) Long buildingId) {
    if (buildingId != null) {
      return ResponseEntity.ok(floorUseCase.findByBuildingId(buildingId));
    }
    return ResponseEntity.ok(floorUseCase.list());
  }

  @GetMapping("/{id}")
  public ResponseEntity<FloorResponseDTO> findById(@PathVariable Long id) {
    return floorUseCase.findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));
  }

  @PostMapping
  public ResponseEntity<FloorResponseDTO> save(
      @RequestParam Long buildingId,
      @Valid @RequestBody FloorRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(floorUseCase.save(request, buildingId, userDetails.getUsername()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<FloorResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody FloorRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.ok(floorUseCase.update(id, request, userDetails.getUsername()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    floorUseCase.deleteById(id);
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
