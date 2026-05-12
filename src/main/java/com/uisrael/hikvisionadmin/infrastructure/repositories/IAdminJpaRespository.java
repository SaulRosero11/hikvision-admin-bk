package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.AdminJpa;

public interface IAdminJpaRespository extends JpaRepository<AdminJpa, Long> {

  Optional<AdminJpa> findByUsername(String username);

  Optional<AdminJpa> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<AdminJpa> findByBuildingsId(Long buildingId);

}
