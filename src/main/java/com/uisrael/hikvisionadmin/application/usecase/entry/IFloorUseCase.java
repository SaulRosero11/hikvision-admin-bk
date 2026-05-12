package com.uisrael.hikvisionadmin.application.usecase.entry;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.presentation.dto.request.FloorRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.FloorResponseDTO;

public interface IFloorUseCase {
  FloorResponseDTO save(FloorRequestDTO request, Long buildingId, String username);

  FloorResponseDTO update(Long id, FloorRequestDTO request, String username);

  Optional<FloorResponseDTO> findById(Long id);

  List<FloorResponseDTO> findByBuildingId(Long buildingId);

  List<FloorResponseDTO> list();

  void deleteById(Long id);
}
