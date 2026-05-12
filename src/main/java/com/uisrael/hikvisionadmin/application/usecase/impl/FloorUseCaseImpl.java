package com.uisrael.hikvisionadmin.application.usecase.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uisrael.hikvisionadmin.application.usecase.entry.IFloorUseCase;
import com.uisrael.hikvisionadmin.domain.entities.Floor;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.domain.repositories.IFloorRepository;
import com.uisrael.hikvisionadmin.presentation.dto.request.FloorRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.FloorResponseDTO;
import com.uisrael.hikvisionadmin.presentation.mappers.IFloorDtoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FloorUseCaseImpl implements IFloorUseCase {

  private final IFloorRepository repository;
  private final IFloorDtoMapper floorMapper;

  @Override
  public Optional<FloorResponseDTO> findById(Long id) {
    return repository.findById(id).map(floorMapper::toResponseDto);
  }

  @Override
  public List<FloorResponseDTO> findByBuildingId(Long buildingId) {
    return repository.findByBuildingId(buildingId).stream()
        .map(floorMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<FloorResponseDTO> list() {
    return repository.findAll().stream()
        .map(floorMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    if (!repository.existsById(id)) {
      throw new DomainException("Cannot delete: Floor not found");
    }
    repository.deleteById(id);
  }

  @Override
  public FloorResponseDTO save(FloorRequestDTO request, Long buildingId, String username) {
    Floor floor = Floor.builder()
        .floorNumber(request.getFloorNumber())
        .name(request.getName())
        .description(request.getDescription())
        .buildingId(buildingId)
        .isActive(true)
        .createdDate(LocalDateTime.now())
        .createdUser(username)
        .build();

    floor.validate();
    Floor savedFloor = repository.save(floor);
    return floorMapper.toResponseDto(savedFloor);
  }

  @Override
  public FloorResponseDTO update(Long id, FloorRequestDTO request, String username) {
    Floor existing = repository.findById(id)
        .orElseThrow(() -> new DomainException("Floor not found with ID: " + id));

    Floor floorToUpdate = Floor.builder()
        .id(existing.getId())
        .floorNumber(request.getFloorNumber())
        .name(request.getName())
        .description(request.getDescription())
        .isActive(request.getIsActive() != null ? request.getIsActive() : existing.getIsActive())
        .buildingId(existing.getBuildingId()) // Protect relationship
        .createdDate(existing.getCreatedDate()) // Protect original audit
        .createdUser(existing.getCreatedUser()) // Protect original audit
        .modifiedDate(LocalDateTime.now())
        .modifiedUser(username)
        .build();

    floorToUpdate.validate();
    Floor updated = repository.save(floorToUpdate);
    return floorMapper.toResponseDto(updated);
  }

}
