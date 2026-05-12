package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.DeviceJpa;

@Repository
public interface IDeviceJpaRepository extends JpaRepository<DeviceJpa, Long> {

  // Busca todos los dispositivos instalados en un piso específico
  List<DeviceJpa> findByFloorId(Long floorId);

  // Búsqueda por código único del dispositivo (ej. S/N o código interno)
  Optional<DeviceJpa> findByCode(String code);

  // Búsqueda por dirección MAC para evitar duplicados en la red
  Optional<DeviceJpa> findByMacAddress(String macAddress);

  // Búsqueda por IP (útil para diagnósticos de conexión)
  Optional<DeviceJpa> findByIp(String ip);

  // En IDeviceJpaRepository
  @Query("SELECT d FROM DeviceJpa d WHERE d.floor.building.id = :buildingId")
  List<DeviceJpa> findByBuildingId(@Param("buildingId") Long buildingId);
}
