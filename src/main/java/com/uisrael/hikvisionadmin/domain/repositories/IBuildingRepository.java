package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.Building;

public interface IBuildingRepository {

  Building save(Building building);

  Building update(Long id, Building building);

  Optional<Building> findById(Long id);

  List<Building> list();

  List<Building> findByAdminId(Long adminId);

  void deleteById(Long id);

  boolean existsById(Long id);

}
