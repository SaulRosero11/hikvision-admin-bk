package com.uisrael.hikvisionadmin.application.usecase.entry;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.presentation.dto.request.UserRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.UserResponseDTO;

public interface IUserUseCase {

  UserResponseDTO save(UserRequestDTO request, String username);

  UserResponseDTO update(Long id, UserRequestDTO request, String username);

  Optional<UserResponseDTO> findById(Long id);

  List<UserResponseDTO> findByBuildingId(Long buildingId);

  void deleteById(Long id);

  // Método clave para la sincronización de pisos
  void syncFloorPermissions(Long userId, List<Long> newFloorIds, String username);
}
