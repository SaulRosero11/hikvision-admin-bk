package com.uisrael.hikvisionadmin.application.usecase.entry;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.presentation.dto.request.BuildingRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.BuildingResponseDTO;

public interface IBuildingUseCase {

  BuildingResponseDTO save(BuildingRequestDTO building, Long adminId, String username);

  BuildingResponseDTO update(Long id, BuildingRequestDTO request, String username);

  Optional<BuildingResponseDTO> findById(Long id);

  List<BuildingResponseDTO> list();

  List<BuildingResponseDTO> findByAdminId(Long adminId);

  void deleteById(Long id);

  boolean existsById(Long id);

}
