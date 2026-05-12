package com.uisrael.hikvisionadmin.presentation.controllers;

import java.util.List;
import java.util.Map;

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

import com.uisrael.hikvisionadmin.application.usecase.entry.IDeviceUseCase;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.infrastructure.security.AdminUserDetails;
import com.uisrael.hikvisionadmin.presentation.dto.request.DeviceRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.DeviceResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.EventReportResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

  private final IDeviceUseCase deviceUseCase;

  @GetMapping
  public ResponseEntity<List<DeviceResponseDTO>> findAll(
      @RequestParam(required = false) Long floorId,
      @RequestParam(required = false) Long buildingId) {

    if (floorId != null) {
      return ResponseEntity.ok(deviceUseCase.findByFloorId(floorId));
    }

    if (buildingId != null) {
      return ResponseEntity.ok(deviceUseCase.findByBuildingId(buildingId));
    }

    return ResponseEntity.ok(deviceUseCase.list());
  }

  @GetMapping("/{id}")
  public ResponseEntity<DeviceResponseDTO> findById(@PathVariable Long id) {
    return deviceUseCase.findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + id));
  }

  @PostMapping
  public ResponseEntity<DeviceResponseDTO> save(
      @RequestParam Long floorId,
      @Valid @RequestBody DeviceRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(deviceUseCase.save(request, floorId, userDetails.getUsername()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<DeviceResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody DeviceRequestDTO request) {
    AdminUserDetails userDetails = getAuthenticatedUser();
    return ResponseEntity.ok(deviceUseCase.update(id, request, userDetails.getUsername()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    deviceUseCase.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/test-connection")
  public ResponseEntity<Map<String, Object>> testConnection(@PathVariable Long id) {
    boolean connected = deviceUseCase.testConnection(id);
    return ResponseEntity.ok(Map.of("connected", connected));
  }

  @GetMapping("/{id}/info")
  public ResponseEntity<Map<String, Object>> getDeviceInfo(@PathVariable Long id) {
    return ResponseEntity.ok(deviceUseCase.getDeviceInfo(id));
  }

  @GetMapping("/{id}/events/report")
  public ResponseEntity<EventReportResponseDTO> getEventsReport(
      @PathVariable Long id,
      @RequestParam String beginTime,
      @RequestParam String endTime) {
    return ResponseEntity.ok(deviceUseCase.generateEventsReport(id, beginTime, endTime));
  }

  @PostMapping("/{id}/events")
  public ResponseEntity<Map<String, Object>> searchAccessEvents(
      @PathVariable Long id,
      @RequestParam(defaultValue = "0") int searchPosition,
      @RequestParam(defaultValue = "30") int maxResults,
      @RequestParam String beginTime,
      @RequestParam String endTime) {
    return ResponseEntity.ok(
        deviceUseCase.searchAccessEvents(id, searchPosition, maxResults, beginTime, endTime));
  }

  private AdminUserDetails getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof AdminUserDetails)) {
      throw new DomainException("No valid administrator session found");
    }
    return (AdminUserDetails) auth.getPrincipal();
  }
}
