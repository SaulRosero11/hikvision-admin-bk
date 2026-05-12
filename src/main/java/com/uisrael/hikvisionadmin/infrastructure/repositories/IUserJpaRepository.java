package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserJpa;

@Repository
public interface IUserJpaRepository extends JpaRepository<UserJpa, Long> {

  // Buscar por identificación (Cédula/DNI) para evitar duplicados
  Optional<UserJpa> findByIdentification(String identification);

  // Listar todos los usuarios de un edificio específico
  List<UserJpa> findByBuildingId(Long buildingId);

  // Verificar existencia por identificación
  boolean existsByIdentification(String identification);
}
