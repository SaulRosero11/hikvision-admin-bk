package com.uisrael.hikvisionadmin.application.usecase.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uisrael.hikvisionadmin.application.usecase.entry.IUserUseCase;
import com.uisrael.hikvisionadmin.domain.entities.Device;
import com.uisrael.hikvisionadmin.domain.entities.User;
import com.uisrael.hikvisionadmin.domain.entities.UserFloorPermission;
import com.uisrael.hikvisionadmin.domain.entities.UserImage;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.domain.repositories.IDeviceRepository;
import com.uisrael.hikvisionadmin.domain.repositories.IUserFloorPermissionRepository;
import com.uisrael.hikvisionadmin.domain.repositories.IUserImageRepository;
import com.uisrael.hikvisionadmin.domain.repositories.IUserRepository;
import com.uisrael.hikvisionadmin.domain.services.IEncryptionService;
import com.uisrael.hikvisionadmin.domain.services.IHikvisionDeviceService;
import com.uisrael.hikvisionadmin.presentation.dto.request.UserRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.UserResponseDTO;
import com.uisrael.hikvisionadmin.presentation.mappers.IUserDtoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserUseCaseImpl implements IUserUseCase {

  private final IUserRepository userRepository;
  private final IUserFloorPermissionRepository permissionRepository;
  private final IUserImageRepository imageRepository;
  private final IDeviceRepository deviceRepository;
  private final IUserDtoMapper userMapper;
  private final IHikvisionDeviceService hikvisionService;
  private final IEncryptionService encryptionService;

  @Override
  public UserResponseDTO save(UserRequestDTO request, String username) {
    User userFromDto = userMapper.toDomain(request);

    List<UserImage> images = userFromDto.getImages() != null
        ? userFromDto.getImages()
        : new ArrayList<>();

    User userToSave = User.builder()
        .identification(userFromDto.getIdentification())
        .fullName(userFromDto.getFullName())
        .buildingId(userFromDto.getBuildingId())
        .gender(userFromDto.getGender())
        .roomNumber(userFromDto.getRoomNumber())
        .floorNumber(userFromDto.getFloorNumber())
        .doorRight(userFromDto.getDoorRight())
        .localUiRight(userFromDto.getLocalUiRight())
        .validity(userFromDto.getValidity())
        .images(images)
        .isActive(true)
        .createdUser(username)
        .createdDate(LocalDateTime.now())
        .build();

    userToSave.validate();

    User savedUser = userRepository.save(userToSave);

    if (request.getFloorIds() != null && !request.getFloorIds().isEmpty()) {
      syncFloorPermissions(savedUser.getId(), request.getFloorIds(), username);
    }

    return userRepository.findById(savedUser.getId())
        .map(userMapper::toResponseDto)
        .orElseThrow(() -> new DomainException("Error retrieving saved user"));
  }

  @Override
  public UserResponseDTO update(Long id, UserRequestDTO request, String username) {
    User existing = userRepository.findById(id)
        .orElseThrow(() -> new DomainException("User not found with ID: " + id));

    User userFromDto = userMapper.toDomain(request);

    User userToUpdate = User.builder()
        .id(existing.getId())
        .identification(userFromDto.getIdentification())
        .fullName(userFromDto.getFullName())
        .buildingId(userFromDto.getBuildingId())
        .gender(userFromDto.getGender())
        .roomNumber(userFromDto.getRoomNumber())
        .floorNumber(userFromDto.getFloorNumber())
        .doorRight(userFromDto.getDoorRight())
        .localUiRight(userFromDto.getLocalUiRight())
        .validity(userFromDto.getValidity())
        .isActive(userFromDto.getIsActive())
        .createdUser(existing.getCreatedUser())
        .createdDate(existing.getCreatedDate())
        .modifiedUser(username)
        .modifiedDate(LocalDateTime.now())
        .build();

    userToUpdate.validate();
    userRepository.save(userToUpdate);

    // Si se envían imágenes nuevas, reemplazar y actualizar en dispositivos
    boolean imagesChanged = false;
    if (request.getImages() != null) {
      imageRepository.deleteByUserId(id);
      for (UserImage img : userMapper.toUserImageDomainList(request.getImages())) {
        UserImage imageToSave = UserImage.builder()
            .imageBase64(img.getImageBase64())
            .imageType(img.getImageType())
            .extension(img.getExtension())
            .userId(id)
            .build();
        imageRepository.save(imageToSave);
      }
      imagesChanged = true;
    }

    if (request.getFloorIds() != null) {
      syncFloorPermissions(id, request.getFloorIds(), username);
    }

    // Si la imagen cambió, actualizar en todos los dispositivos de los pisos asignados
    if (imagesChanged) {
      updateFaceOnAllAssignedDevices(id);
    }

    return userRepository.findById(id)
        .map(userMapper::toResponseDto)
        .orElseThrow(() -> new DomainException("Error retrieving updated user"));
  }

  @Override
  public void syncFloorPermissions(Long userId, List<Long> newFloorIds, String username) {
    List<UserFloorPermission> currentPermissions = permissionRepository.findByUserId(userId);
    List<Long> currentFloorIds = currentPermissions.stream()
        .map(UserFloorPermission::getFloorId)
        .collect(Collectors.toList());

    List<Long> floorsToAdd = newFloorIds.stream()
        .filter(id -> !currentFloorIds.contains(id))
        .collect(Collectors.toList());

    List<Long> floorsToRemove = currentFloorIds.stream()
        .filter(id -> !newFloorIds.contains(id))
        .collect(Collectors.toList());

    // ALTAS: Crear usuario + enviar foto en cada dispositivo del piso
    for (Long floorId : floorsToAdd) {
      UserFloorPermission newPermission = UserFloorPermission.builder()
          .userId(userId)
          .floorId(floorId)
          .accessAllowed(true)
          .isSynced(false)
          .isActive(true)
          .assignedDate(LocalDateTime.now())
          .createdUser(username)
          .createdDate(LocalDateTime.now())
          .build();
      permissionRepository.save(newPermission);

      enrollUserOnFloorDevices(userId, floorId);
    }

    // BAJAS: Eliminar usuario + foto de cada dispositivo del piso
    for (Long floorId : floorsToRemove) {
      removeUserFromFloorDevices(userId, floorId);
      permissionRepository.deleteByUserIdAndFloorId(userId, floorId);
    }
  }

  /**
   * Inscribe al usuario en TODOS los dispositivos del piso:
   * 1. Crea la persona en el dispositivo (UserInfo/Record)
   * 2. Sube la foto facial (FaceDataRecord/Record)
   */
  private void enrollUserOnFloorDevices(Long userId, Long floorId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new DomainException("User not found: " + userId));
    List<Device> devices = deviceRepository.findByFloorId(floorId);
    String faceBase64 = getFirstFaceImage(userId);

    for (Device device : devices) {
      try {
        String decryptedPass = encryptionService.decrypt(device.getDevicePassword());
        String employeeNo = user.getIdentification();

        // Si el usuario ya existe en el dispositivo, solo actualizar
        boolean exists = hikvisionService.userExistsOnDevice(
            device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass, employeeNo);

        if (!exists) {
          hikvisionService.createUserOnDevice(
              device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass,
              employeeNo, user.getFullName(), "normal",
              user.getDoorRight() != null ? user.getDoorRight() : "1",
              user.getLocalUiRight() != null && user.getLocalUiRight());
        }

        // Subir foto facial
        if (faceBase64 != null) {
          boolean faceExists = hikvisionService.faceExistsOnDevice(
              device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass, employeeNo);
          if (faceExists) {
            hikvisionService.modifyFaceOnDevice(
                device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass, employeeNo, faceBase64);
          } else {
            hikvisionService.uploadFaceToDevice(
                device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass, employeeNo, faceBase64);
          }
        }

        log.info("User {} enrolled on device {} (floor {})", employeeNo, device.getIp(), floorId);
      } catch (Exception e) {
        log.error("Failed to enroll user {} on device {} - {}", userId, device.getIp(), e.getMessage());
      }
    }
  }

  /**
   * Elimina al usuario de TODOS los dispositivos del piso:
   * 1. Elimina la foto facial (FaceDataRecord/Delete)
   * 2. Elimina la persona (UserInfoDetail/Delete)
   */
  private void removeUserFromFloorDevices(Long userId, Long floorId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new DomainException("User not found: " + userId));
    List<Device> devices = deviceRepository.findByFloorId(floorId);

    for (Device device : devices) {
      try {
        String decryptedPass = encryptionService.decrypt(device.getDevicePassword());
        String employeeNo = user.getIdentification();

        // Verificar si el usuario tiene permisos en otros pisos del mismo dispositivo
        // Si tiene otros pisos asignados que comparten dispositivos, no eliminar
        boolean hasOtherPermissions = hasOtherFloorPermissions(userId, floorId);

        if (!hasOtherPermissions) {
          hikvisionService.deleteFaceFromDevice(
              device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass, employeeNo);
          hikvisionService.deleteUserFromDevice(
              device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass, employeeNo);
          log.info("User {} removed from device {} (floor {})", employeeNo, device.getIp(), floorId);
        }
      } catch (Exception e) {
        log.error("Failed to remove user {} from device {} - {}", userId, device.getIp(), e.getMessage());
      }
    }
  }

  /**
   * Actualiza la foto facial en todos los dispositivos donde el usuario tiene permisos activos.
   */
  private void updateFaceOnAllAssignedDevices(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new DomainException("User not found: " + userId));
    String faceBase64 = getFirstFaceImage(userId);
    if (faceBase64 == null) return;

    List<UserFloorPermission> permissions = permissionRepository.findByUserId(userId);
    for (UserFloorPermission permission : permissions) {
      List<Device> devices = deviceRepository.findByFloorId(permission.getFloorId());
      for (Device device : devices) {
        try {
          String decryptedPass = encryptionService.decrypt(device.getDevicePassword());
          hikvisionService.modifyFaceOnDevice(
              device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPass,
              user.getIdentification(), faceBase64);
        } catch (Exception e) {
          log.error("Failed to update face for user {} on device {} - {}", userId, device.getIp(), e.getMessage());
        }
      }
    }
  }

  private boolean hasOtherFloorPermissions(Long userId, Long excludeFloorId) {
    return permissionRepository.findByUserId(userId).stream()
        .anyMatch(p -> !p.getFloorId().equals(excludeFloorId));
  }

  private String getFirstFaceImage(Long userId) {
    List<UserImage> images = imageRepository.findByUserId(userId);
    if (images != null && !images.isEmpty()) {
      return images.get(0).getImageBase64();
    }
    return null;
  }

  @Override
  public Optional<UserResponseDTO> findById(Long id) {
    return userRepository.findById(id).map(userMapper::toResponseDto);
  }

  @Override
  public List<UserResponseDTO> findByBuildingId(Long buildingId) {
    return userRepository.findByBuildingId(buildingId).stream()
        .map(userMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new DomainException("Cannot delete: User not found");
    }

    // Eliminar de todos los dispositivos antes de borrar de DB
    List<UserFloorPermission> permissions = permissionRepository.findByUserId(id);
    for (UserFloorPermission permission : permissions) {
      removeUserFromFloorDevices(id, permission.getFloorId());
    }

    userRepository.deleteById(id);
  }
}
