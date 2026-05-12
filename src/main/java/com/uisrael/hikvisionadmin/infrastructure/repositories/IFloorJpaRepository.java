package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.FloorJpa;

@Repository
public interface IFloorJpaRepository extends JpaRepository<FloorJpa, Long> {

  // Busca todos los pisos de un edificio específico usando el ID del Building
  List<FloorJpa> findByBuildingId(Long buildingId);

  // Opcional: Buscar por número de piso dentro de un edificio
  List<FloorJpa> findByBuildingIdAndFloorNumber(Long buildingId, Integer floorNumber);

}
