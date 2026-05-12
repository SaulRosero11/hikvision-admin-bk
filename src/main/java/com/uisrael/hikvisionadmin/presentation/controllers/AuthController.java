package com.uisrael.hikvisionadmin.presentation.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uisrael.hikvisionadmin.application.usecase.entry.IAuthUseCase;
import com.uisrael.hikvisionadmin.presentation.dto.request.AdminRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.request.ChangePasswordRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.request.LoginRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.AdminResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.LoginResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final IAuthUseCase authUseCase;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
    return ResponseEntity.ok(authUseCase.login(request));
  }

  @PostMapping("/register")
  public ResponseEntity<AdminResponseDTO> registrar(@Valid @RequestBody AdminRequestDTO request) {
    return ResponseEntity.ok(authUseCase.registerAdmin(request));
  }

  @GetMapping("/me")
  public ResponseEntity<AdminResponseDTO> obtenerPerfil(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(authUseCase.getNowAdmin(userDetails.getUsername()));
  }

  @PostMapping("/cambiar-password")
  public ResponseEntity<Map<String, String>> cambiarPassword(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody ChangePasswordRequestDTO request) {
    authUseCase.changePassword(
        userDetails.getUsername(),
        request.getPasswordActual(),
        request.getPasswordNuevo());
    return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente"));
  }

}
