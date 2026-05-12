package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.UserFloorPermission;

public interface IUserFloorPermissionRepository {

  UserFloorPermission save(UserFloorPermission permission);

  Optional<UserFloorPermission> findById(Long id);

  Optional<UserFloorPermission> findByUserIdAndFloorId(Long userId, Long floorId);

  List<UserFloorPermission> findByUserId(Long userId);

  List<UserFloorPermission> findByFloorId(Long floorId);

  List<UserFloorPermission> findAll();

  void deleteById(Long id);

  void deleteByUserId(Long userId);

  void deleteByUserIdAndFloorId(Long userId, Long floorId);

  boolean existsById(Long id);
}
