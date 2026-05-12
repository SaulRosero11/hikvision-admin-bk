package com.uisrael.hikvisionadmin.application.usecase.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uisrael.hikvisionadmin.application.usecase.entry.IAuthUseCase;
import com.uisrael.hikvisionadmin.domain.entities.Admin;
import com.uisrael.hikvisionadmin.domain.entities.Building;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.infrastructure.persistence.adapters.AdminRepositoryImpl;
import com.uisrael.hikvisionadmin.infrastructure.persistence.adapters.BuildingRepositoryImpl;
import com.uisrael.hikvisionadmin.infrastructure.security.JwtService;
import com.uisrael.hikvisionadmin.presentation.dto.request.AdminRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.request.LoginRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.AdminResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.LoginResponseDTO;
import com.uisrael.hikvisionadmin.presentation.mappers.IAuthDtoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUseCaseImpl implements IAuthUseCase {

  private final AdminRepositoryImpl adminRepository;
  private final BuildingRepositoryImpl buildingRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final IAuthDtoMapper mapper;

  @Override
  public LoginResponseDTO login(LoginRequestDTO request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    Admin admin = adminRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new DomainException("Admin not found"));

    Admin adminUpdated = Admin.builder()
        .id(admin.getId())
        .username(admin.getUsername())
        .password(admin.getPassword())
        .email(admin.getEmail())
        .fullName(admin.getFullName())
        .isActive(admin.getIsActive())
        .createdDate(admin.getCreatedDate())
        .lastAccess(LocalDateTime.now())
        .buildings(admin.getBuildings())
        .build();

    adminRepository.save(adminUpdated);

    List<Long> buildingIds = admin.getBuildings().stream()
        .map(Building::getId)
        .collect(Collectors.toList());

    String token = jwtService.generateToken(
        admin.getUsername(),
        admin.getId(),
        buildingIds);

    return LoginResponseDTO.builder()
        .token(token)
        .tipo("Bearer")
        .administradorId(admin.getId())
        .username(admin.getUsername())
        .email(admin.getEmail())
        .nombreCompleto(admin.getFullName())
        .expiracion(jwtService.getExpirationTime())
        .build();
  }

  @Override
  public AdminResponseDTO registerAdmin(AdminRequestDTO request) {
    if (adminRepository.existsByUsername(request.getUsername())) {
      throw new DomainException("El username ya está en uso");
    }

    if (adminRepository.existsByEmail(request.getEmail())) {
      throw new DomainException("El email ya está registrado");
    }

    List<Building> initialBuildings = new ArrayList<>();
    List<Long> buildingIds = new ArrayList<>();

    if (request.getBuildingId() != null) {
      Building edificio = buildingRepository.findById(request.getBuildingId())
          .orElseThrow(() -> new DomainException("Edificio no encontrado"));
      initialBuildings.add(edificio);
      buildingIds.add(edificio.getId());
    }

    Admin admin = Admin.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .email(request.getEmail())
        .fullName(request.getFullName())
        .isActive(true)
        .createdDate(LocalDateTime.now())
        .lastAccess(LocalDateTime.now())
        .buildings(initialBuildings)
        .build();

    Admin saved = adminRepository.save(admin);

    String token = jwtService.generateToken(saved.getUsername(), saved.getId(), buildingIds);

    AdminResponseDTO response = mapper.toResponseDTO(saved);
    response.setToken(token);
    response.setTipo("Bearer");
    response.setExpiracion(jwtService.getExpirationTime());

    return response;
  }

  @Override
  public AdminResponseDTO getNowAdmin(String username) {
    Admin admin = adminRepository.findByUsername(username)
        .orElseThrow(() -> new DomainException("Administrador no encontrado"));

    return mapper.toResponseDTO(admin);
  }

  @Override
  public void changePassword(String username, String passwordActual, String passwordNuevo) {

    Admin admin = adminRepository.findByUsername(username)
        .orElseThrow(() -> new DomainException("Administrador no encontrado"));

    if (!passwordEncoder.matches(passwordActual, admin.getPassword())) {
      throw new DomainException("La contraseña actual es incorrecta");
    }

    Admin adminActualizado = Admin.builder()
        .id(admin.getId())
        .username(admin.getUsername())
        .password(passwordEncoder.encode(passwordNuevo))
        .email(admin.getEmail())
        .fullName(admin.getFullName())
        .isActive(admin.getIsActive())
        .createdDate(admin.getCreatedDate())
        .lastAccess(admin.getLastAccess())
        .buildings(admin.getBuildings())
        .modifiedDate(LocalDateTime.now())
        .modifiedUser(username)
        .build();

    adminRepository.save(adminActualizado);
  }

}
