package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.UserFloorPermission;
import com.uisrael.hikvisionadmin.domain.repositories.IUserFloorPermissionRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IUserFloorPermissionMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IUserFloorPermissionJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFloorPermissionRepositoryImpl implements IUserFloorPermissionRepository {

  private final IUserFloorPermissionJpaRepository jpaRepository;
  private final IUserFloorPermissionMapper entityMapper;

  @Override
  public UserFloorPermission save(UserFloorPermission permission) {
    var entity = entityMapper.toEntity(permission);
    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<UserFloorPermission> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public Optional<UserFloorPermission> findByUserIdAndFloorId(Long userId, Long floorId) {
    return jpaRepository.findByUserIdAndFloorId(userId, floorId).map(entityMapper::toDomain);
  }

  @Override
  public List<UserFloorPermission> findByUserId(Long userId) {
    return jpaRepository.findByUserId(userId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<UserFloorPermission> findByFloorId(Long floorId) {
    return jpaRepository.findByFloorId(floorId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<UserFloorPermission> findAll() {
    return jpaRepository.findAll().stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId(Long userId) {
    jpaRepository.deleteByUserId(userId);
  }

  @Override
  public void deleteByUserIdAndFloorId(Long userId, Long floorId) {
    jpaRepository.deleteByUserIdAndFloorId(userId, floorId);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }
}
