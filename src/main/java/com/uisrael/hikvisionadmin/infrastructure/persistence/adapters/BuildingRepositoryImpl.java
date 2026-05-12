package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.Building;
import com.uisrael.hikvisionadmin.domain.repositories.IBuildingRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IBuildingJpaMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IBuildingJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuildingRepositoryImpl implements IBuildingRepository {

  private final IBuildingJpaRepository jpaRepository;
  private final IBuildingJpaMapper entityMapper;

  @Override
  public Building save(Building building) {
    var entity = entityMapper.toEntity(building);
    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Building update(Long id, Building building) {
    var entitieExist = jpaRepository.findById(id)
        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Edificio no encontrado con id: " + id));

    entitieExist.setName(building.getName());
    entitieExist.setAddress(building.getAddress());
    entitieExist.setNumberOfFloors(building.getNumberOfFloors());
    entitieExist.setDescription(building.getDescription());
    entitieExist.setPhone(building.getPhone());
    entitieExist.setIsActive(building.getIsActive());
    entitieExist.setModifiedUser(building.getModifiedUser());
    entitieExist.setModifiedDate(building.getModifiedDate());

    var saved = jpaRepository.save(entitieExist);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<Building> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public List<Building> list() {
    return jpaRepository.findAll().stream().map(entityMapper::toDomain).toList();
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }

  @Override
  public List<Building> findByAdminId(Long adminId) {
    return jpaRepository.findByAdminId(adminId)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

}
