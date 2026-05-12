package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.Device;
import com.uisrael.hikvisionadmin.domain.repositories.IDeviceRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IDeviceJpaMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IDeviceJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements IDeviceRepository {

  private final IDeviceJpaRepository jpaRepository;
  private final IDeviceJpaMapper entityMapper;

  @Override
  public Device save(Device device) {
    var entity = entityMapper.toEntity(device);
    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<Device> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public Optional<Device> findByCode(String code) {
    return jpaRepository.findByCode(code).map(entityMapper::toDomain);
  }

  @Override
  public Optional<Device> findByIp(String ip) {
    return jpaRepository.findByIp(ip).map(entityMapper::toDomain);
  }

  @Override
  public Optional<Device> findByMacAddress(String macAddress) {
    return jpaRepository.findByMacAddress(macAddress).map(entityMapper::toDomain);
  }

  @Override
  public Device update(Long id, Device device) {
    var existingEntity = jpaRepository.findById(id)
        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Device not found with id: " + id));

    existingEntity.setCode(device.getCode());
    existingEntity.setIp(device.getIp());
    existingEntity.setPort(device.getPort());
    existingEntity.setModel(device.getModel());
    existingEntity.setLocation(device.getLocation());

    existingEntity.setDeviceUser(device.getDeviceUser());
    existingEntity.setDevicePassword(device.getDevicePassword());
    existingEntity.setMacAddress(device.getMacAddress());
    existingEntity.setSerialNumber(device.getSerialNumber());
    existingEntity.setDeviceType(device.getDeviceType());
    existingEntity.setFirmwareVersion(device.getFirmwareVersion());
    existingEntity.setIsEnabled(device.getIsEnabled());

    existingEntity.setIsActive(device.getIsActive());
    existingEntity.setModifiedUser(device.getModifiedUser());
    existingEntity.setModifiedDate(device.getModifiedDate());

    var saved = jpaRepository.save(existingEntity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public List<Device> findByFloorId(Long floorId) {
    return jpaRepository.findByFloorId(floorId)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<Device> findAll() {
    return jpaRepository.findAll()
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }

  @Override
  public List<Device> findByBuildingId(Long buildingId) {
    return jpaRepository.findByBuildingId(buildingId)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

}
