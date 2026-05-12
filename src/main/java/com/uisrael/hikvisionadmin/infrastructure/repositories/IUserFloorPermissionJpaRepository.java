package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserFloorPermissionJpa;

@Repository
public interface IUserFloorPermissionJpaRepository extends JpaRepository<UserFloorPermissionJpa, Long> {

  // Para obtener todos los permisos de un usuario y comparar (Diff)
  List<UserFloorPermissionJpa> findByUserId(Long userId);

  // Para obtener todos los usuarios asignados a un piso (Sincronización masiva)
  List<UserFloorPermissionJpa> findByFloorId(Long floorId);

  // Buscar un permiso específico
  Optional<UserFloorPermissionJpa> findByUserIdAndFloorId(Long userId, Long floorId);

  // Eliminar todos los permisos de un usuario (cuando se va del edificio)
  void deleteByUserId(Long userId);

  // Eliminar un acceso específico de un piso
  void deleteByUserIdAndFloorId(Long userId, Long floorId);

}
