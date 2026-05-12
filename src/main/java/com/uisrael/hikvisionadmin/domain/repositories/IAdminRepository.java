package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.Admin;

public interface IAdminRepository {

  Admin save(Admin admin);

  Optional<Admin> findByUsername(String username);

  Optional<Admin> findByBuildingId(Long buildingId);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<Admin> findById(Long id);

}
