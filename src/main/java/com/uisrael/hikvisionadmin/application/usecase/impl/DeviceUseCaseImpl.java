package com.uisrael.hikvisionadmin.application.usecase.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uisrael.hikvisionadmin.application.usecase.entry.IDeviceUseCase;
import com.uisrael.hikvisionadmin.domain.entities.Device;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;
import com.uisrael.hikvisionadmin.domain.repositories.IDeviceRepository;
import com.uisrael.hikvisionadmin.domain.services.IEncryptionService;
import com.uisrael.hikvisionadmin.domain.services.IHikvisionDeviceService;
import com.uisrael.hikvisionadmin.presentation.dto.request.DeviceRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.DeviceResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.EventReportResponseDTO;
import com.uisrael.hikvisionadmin.presentation.mappers.IDeviceDtoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeviceUseCaseImpl implements IDeviceUseCase {

  private final IDeviceRepository repository;
  private final IDeviceDtoMapper deviceMapper;
  private final IEncryptionService encryptionService;
  private final IHikvisionDeviceService hikvisionService;

  @Override
  public DeviceResponseDTO save(DeviceRequestDTO request, Long floorId, String username) {
    int port = request.getPort() != null ? request.getPort() : 80;

    // Probar conexión y obtener info del dispositivo
    Map<String, Object> deviceInfo = hikvisionService.getDeviceInfo(
        request.getIp(), port, request.getDeviceUser(), request.getDevicePassword());

    if (deviceInfo == null || deviceInfo.isEmpty() || !Boolean.TRUE.equals(deviceInfo.get("connected"))) {
      throw new DomainException("Cannot add device: connection test failed for " + request.getIp());
    }

    // Auto-completar campos desde deviceInfo si no vienen en el request
    String model = request.getModel() != null ? request.getModel()
        : (String) deviceInfo.get("model");
    String serialNumber = request.getSerialNumber() != null ? request.getSerialNumber()
        : (String) deviceInfo.get("serialNumber");
    String macAddress = request.getMacAddress() != null ? request.getMacAddress()
        : (String) deviceInfo.get("macAddress");
    String firmwareVersion = request.getFirmwareVersion() != null ? request.getFirmwareVersion()
        : (String) deviceInfo.get("firmwareVersion");

    Device device = Device.builder()
        .code(request.getCode())
        .ip(request.getIp())
        .port(port)
        .model(model)
        .location(request.getLocation())
        .deviceUser(request.getDeviceUser())
        .devicePassword(encryptionService.encrypt(request.getDevicePassword()))
        .macAddress(macAddress)
        .serialNumber(serialNumber)
        .deviceType(request.getDeviceType())
        .firmwareVersion(firmwareVersion)
        .isEnabled(true)
        .floorId(floorId)
        .isActive(true)
        .createdDate(LocalDateTime.now())
        .createdUser(username)
        .build();

    device.validate();
    Device savedDevice = repository.save(device);
    return toResponseWithDecryptedPassword(savedDevice);
  }

  @Override
  public DeviceResponseDTO update(Long id, DeviceRequestDTO request, String username) {
    Device existing = repository.findById(id)
        .orElseThrow(() -> new DomainException("Device not found with ID: " + id));

    Device deviceToUpdate = Device.builder()
        .id(existing.getId())
        .code(request.getCode())
        .ip(request.getIp())
        .port(request.getPort())
        .model(request.getModel())
        .location(request.getLocation())
        .deviceUser(request.getDeviceUser())
        .devicePassword(encryptionService.encrypt(request.getDevicePassword()))
        .macAddress(request.getMacAddress())
        .serialNumber(request.getSerialNumber())
        .deviceType(request.getDeviceType())
        .firmwareVersion(request.getFirmwareVersion())
        .isEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : existing.getIsEnabled())
        .isActive(request.getIsActive() != null ? request.getIsActive() : existing.getIsActive())
        .floorId(existing.getFloorId())
        .createdDate(existing.getCreatedDate())
        .createdUser(existing.getCreatedUser())
        .modifiedDate(LocalDateTime.now())
        .modifiedUser(username)
        .build();

    deviceToUpdate.validate();
    Device updated = repository.save(deviceToUpdate);
    return toResponseWithDecryptedPassword(updated);
  }

  @Override
  public Optional<DeviceResponseDTO> findById(Long id) {
    return repository.findById(id).map(this::toResponseWithDecryptedPassword);
  }

  @Override
  public List<DeviceResponseDTO> findByFloorId(Long floorId) {
    return repository.findByFloorId(floorId).stream()
        .map(this::toResponseWithDecryptedPassword)
        .collect(Collectors.toList());
  }

  @Override
  public List<DeviceResponseDTO> list() {
    return repository.findAll().stream()
        .map(this::toResponseWithDecryptedPassword)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    if (!repository.existsById(id)) {
      throw new DomainException("Cannot delete: Device not found");
    }
    repository.deleteById(id);
  }

  @Override
  public List<DeviceResponseDTO> findByBuildingId(Long buildingId) {
    return repository.findByBuildingId(buildingId).stream()
        .map(this::toResponseWithDecryptedPassword)
        .collect(Collectors.toList());
  }

  @Override
  public boolean testConnection(Long deviceId) {
    Device device = repository.findById(deviceId)
        .orElseThrow(() -> new DomainException("Device not found with ID: " + deviceId));
    String decryptedPassword = encryptionService.decrypt(device.getDevicePassword());
    return hikvisionService.testConnection(device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPassword);
  }

  @Override
  public Map<String, Object> getDeviceInfo(Long deviceId) {
    Device device = repository.findById(deviceId)
        .orElseThrow(() -> new DomainException("Device not found with ID: " + deviceId));
    String decryptedPassword = encryptionService.decrypt(device.getDevicePassword());
    return hikvisionService.getDeviceInfo(device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPassword);
  }

  @Override
  public Map<String, Object> searchAccessEvents(Long deviceId, int searchPosition, int maxResults,
      String beginTime, String endTime) {
    Device device = repository.findById(deviceId)
        .orElseThrow(() -> new DomainException("Device not found with ID: " + deviceId));
    String decryptedPassword = encryptionService.decrypt(device.getDevicePassword());
    Map<String, Object> result = hikvisionService.searchAccessEvents(
        device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPassword,
        searchPosition, maxResults, beginTime, endTime);
    enrichEventsWithImages(result, device.getDeviceUser(), decryptedPassword);
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public EventReportResponseDTO generateEventsReport(Long deviceId, String beginTime, String endTime) {
    Device device = repository.findById(deviceId)
        .orElseThrow(() -> new DomainException("Device not found with ID: " + deviceId));
    String decryptedPassword = encryptionService.decrypt(device.getDevicePassword());

    String begin = beginTime;
    String end = endTime;

    // Paginar para obtener todos los eventos de los últimos 30 días
    List<Map<String, Object>> allEvents = new ArrayList<>();
    int pageSize = 100;
    int position = 0;

    while (true) {
      Map<String, Object> result = hikvisionService.searchAccessEvents(
          device.getIp(), device.getPort(), device.getDeviceUser(), decryptedPassword,
          position, pageSize, begin, end);

      Map<String, Object> acsEvent = (Map<String, Object>) result.get("AcsEvent");
      if (acsEvent == null) break;

      List<Map<String, Object>> infoList = (List<Map<String, Object>>) acsEvent.get("InfoList");
      if (infoList == null || infoList.isEmpty()) break;

      allEvents.addAll(infoList);

      Object totalMatches = acsEvent.get("totalMatches");
      int total = totalMatches != null ? Integer.parseInt(totalMatches.toString()) : 0;
      position += infoList.size();
      if (position >= total) break;
    }

    // Agregar resultados: válidos por modo de verificación, inválidos sin modo
    int validCount = 0;
    int invalidCount = 0;
    Map<String, Integer> byVerifyMode = new LinkedHashMap<>();

    for (Map<String, Object> event : allEvents) {
      Object verifyModeObj = event.get("currentVerifyMode");
      Object employeeNoObj = event.get("employeeNoString");

      String verifyMode = verifyModeObj instanceof String s ? s.trim() : "";
      String employeeNo = employeeNoObj instanceof String s ? s.trim() : "";

      if (!verifyMode.isBlank() && !employeeNo.isBlank()) {
        validCount++;
        byVerifyMode.merge(verifyMode, 1, Integer::sum);
      } else {
        invalidCount++;
      }
    }

    return EventReportResponseDTO.builder()
        .from(begin)
        .to(end)
        .totalEvents(allEvents.size())
        .validCount(validCount)
        .invalidCount(invalidCount)
        .byVerifyMode(byVerifyMode)
        .build();
  }

  @SuppressWarnings("unchecked")
  private void enrichEventsWithImages(Map<String, Object> result, String user, String password) {
    Map<String, Object> acsEvent = (Map<String, Object>) result.get("AcsEvent");
    if (acsEvent == null) return;

    List<Map<String, Object>> infoList = (List<Map<String, Object>>) acsEvent.get("InfoList");
    if (infoList == null || infoList.isEmpty()) return;

    for (Map<String, Object> event : infoList) {
      Object pictureUrl = event.get("pictureURL");
      if (!(pictureUrl instanceof String url) || url.isBlank()) continue;

      try {
        byte[] imageBytes = hikvisionService.downloadImage(url, user, password);
        if (imageBytes.length > 0) {
          event.put("pictureBase64", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes));
        }
      } catch (Exception e) {
        log.warn("Could not download event image from {}: {}", url, e.getMessage());
      }
    }
  }

  private DeviceResponseDTO toResponseWithDecryptedPassword(Device device) {
    DeviceResponseDTO dto = deviceMapper.toResponseDto(device);
    dto.setDevicePassword(encryptionService.decrypt(device.getDevicePassword()));
    return dto;
  }
}
