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

import com.uisrael.hikvisionadmin.application.usecase.entry.IUserUseCase;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.infrastructure.security.AdminUserDetails;
import com.uisrael.hikvisionadmin.presentation.dto.request.UserRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.UserResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final IUserUseCase userUseCase;

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> findByBuildingId(@RequestParam Long buildingId) {
    return ResponseEntity.ok(userUseCase.findByBuildingId(buildingId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
    return userUseCase.findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userUseCase.save(request, userDetails.getUsername()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody UserRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.ok(userUseCase.update(id, request, userDetails.getUsername()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userUseCase.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/floor-permissions")
  public ResponseEntity<Void> syncFloorPermissions(
      @PathVariable Long id,
      @RequestBody List<Long> floorIds) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    userUseCase.syncFloorPermissions(id, floorIds, userDetails.getUsername());
    return ResponseEntity.ok().build();
  }

  private AdminUserDetails getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof AdminUserDetails)) {
      throw new DomainException("No valid administrator session found");
    }
    return (AdminUserDetails) auth.getPrincipal();
  }
}
