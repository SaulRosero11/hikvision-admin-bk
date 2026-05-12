package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.Admin;
import com.uisrael.hikvisionadmin.domain.repositories.IAdminRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IAdminJpaMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IAdminJpaRespository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminRepositoryImpl implements IAdminRepository {

  private final IAdminJpaRespository jpaRepository;
  private final IAdminJpaMapper entityMapper;

  @Override
  public Admin save(Admin admin) {
    var entity = entityMapper.toEntity(admin);
    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<Admin> findByUsername(String username) {
    return jpaRepository.findByUsername(username).map(entityMapper::toDomain);
  }

  @Override
  public Optional<Admin> findByBuildingId(Long buildingId) {
    return jpaRepository.findByBuildingsId(buildingId).map(entityMapper::toDomain);
  }

  @Override
  public boolean existsByUsername(String username) {
    return jpaRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  @Override
  public Optional<Admin> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

}
