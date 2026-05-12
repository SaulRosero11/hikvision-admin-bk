package com.uisrael.hikvisionadmin.application.usecase.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uisrael.hikvisionadmin.application.usecase.entry.IBuildingUseCase;
import com.uisrael.hikvisionadmin.domain.entities.Building;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.domain.repositories.IBuildingRepository;
import com.uisrael.hikvisionadmin.presentation.dto.request.BuildingRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.BuildingResponseDTO;
import com.uisrael.hikvisionadmin.presentation.mappers.IBuildingDtoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingUseCaseImpl implements IBuildingUseCase {

  private final IBuildingRepository repository;
  private final IBuildingDtoMapper buildingMapper;

  @Override
  public BuildingResponseDTO save(BuildingRequestDTO request, Long adminId, String username) {

    System.out.print(adminId);

    Building building = Building.builder()
        .name(request.getName())
        .address(request.getAddress())
        .numberOfFloors(request.getNumberOfFloors())
        .description(request.getDescription())
        .phone(request.getPhone())
        .isActive(true)
        .createdDate(LocalDateTime.now())
        .createdUser(username)
        .adminId(adminId)
        .build();

    building.validate();

    Building savedBuilding = repository.save(building);
    return buildingMapper.toResponseDto(savedBuilding);
  }

  @Override
  public BuildingResponseDTO update(Long id, BuildingRequestDTO request, String username) {
    Building existing = repository.findById(id)
        .orElseThrow(() -> new DomainException("Edificio no encontrado con ID: " + id));

    Building buildingToUpdate = Building.builder()
        .id(existing.getId())
        .name(request.getName())
        .address(request.getAddress())
        .numberOfFloors(request.getNumberOfFloors())
        .description(request.getDescription())
        .phone(request.getPhone())
        .isActive(request.getIsActive() != null ? request.getIsActive() : existing.getIsActive())
        .adminId(existing.getAdminId())
        .createdDate(existing.getCreatedDate())
        .createdUser(existing.getCreatedUser())
        .modifiedDate(LocalDateTime.now())
        .modifiedUser(username)
        .build();

    buildingToUpdate.validate();
    Building updated = repository.save(buildingToUpdate);
    return buildingMapper.toResponseDto(updated);
  }

  @Override
  public Optional<BuildingResponseDTO> findById(Long id) {
    return repository.findById(id)
        .map(buildingMapper::toResponseDto);
  }

  @Override
  public List<BuildingResponseDTO> list() {
    return repository.list().stream()
        .map(buildingMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<BuildingResponseDTO> findByAdminId(Long adminId) {
    return repository.findByAdminId(adminId).stream()
        .map(buildingMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    if (!repository.existsById(id)) {
      throw new DomainException("No se puede eliminar: Edificio no encontrado");
    }
    repository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return repository.existsById(id);
  }

}
