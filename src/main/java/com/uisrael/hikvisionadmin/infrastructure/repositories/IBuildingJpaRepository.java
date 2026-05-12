package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.BuildingJpa;

public interface IBuildingJpaRepository extends JpaRepository<BuildingJpa, Long> {

  List<BuildingJpa> findByAdminId(Long adminId);

}
