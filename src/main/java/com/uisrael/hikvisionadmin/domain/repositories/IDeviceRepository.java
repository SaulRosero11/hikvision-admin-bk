package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.Device;

public interface IDeviceRepository {

  Device save(Device device);

  Optional<Device> findById(Long id);

  Optional<Device> findByCode(String code);

  Device update(Long id, Device building);

  Optional<Device> findByIp(String ip);

  Optional<Device> findByMacAddress(String macAddress);

  List<Device> findByFloorId(Long floorId);

  List<Device> findAll();

  void deleteById(Long id);

  boolean existsById(Long id);

  List<Device> findByBuildingId(Long buildingId);

}
