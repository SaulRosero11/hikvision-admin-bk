package com.uisrael.hikvisionadmin.application.usecase.entry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uisrael.hikvisionadmin.presentation.dto.request.DeviceRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.DeviceResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.EventReportResponseDTO;

public interface IDeviceUseCase {

  DeviceResponseDTO save(DeviceRequestDTO request, Long floorId, String username);

  DeviceResponseDTO update(Long id, DeviceRequestDTO request, String username);

  Optional<DeviceResponseDTO> findById(Long id);

  List<DeviceResponseDTO> findByFloorId(Long floorId);

  List<DeviceResponseDTO> list();

  void deleteById(Long id);

  List<DeviceResponseDTO> findByBuildingId(Long buildingId);

  boolean testConnection(Long deviceId);

  Map<String, Object> getDeviceInfo(Long deviceId);

  Map<String, Object> searchAccessEvents(Long deviceId, int searchPosition, int maxResults,
      String beginTime, String endTime);

  EventReportResponseDTO generateEventsReport(Long deviceId, String beginTime, String endTime);
}
