package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.Floor;

public interface IFloorRepository {

  Floor save(Floor floor);

  Optional<Floor> findById(Long id);

  Floor update(Long id, Floor building);

  List<Floor> findByBuildingId(Long buildingId);

  List<Floor> findAll();

  void deleteById(Long id);

  boolean existsById(Long id);

}
