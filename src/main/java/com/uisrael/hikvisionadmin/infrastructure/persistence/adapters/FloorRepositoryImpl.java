package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.Floor;
import com.uisrael.hikvisionadmin.domain.repositories.IFloorRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IFloorJpaMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IFloorJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FloorRepositoryImpl implements IFloorRepository {

  private final IFloorJpaRepository jpaRepository;
  private final IFloorJpaMapper entityMapper;

  @Override
  public Floor save(Floor floor) {
    var entity = entityMapper.toEntity(floor);
    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<Floor> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public List<Floor> findByBuildingId(Long buildingId) {
    return jpaRepository.findByBuildingId(buildingId)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public Floor update(Long id, Floor floor) {
    var existingEntity = jpaRepository.findById(id)
        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Floor not found with id: " + id));

    existingEntity.setFloorNumber(floor.getFloorNumber());
    existingEntity.setName(floor.getName());
    existingEntity.setDescription(floor.getDescription());
    existingEntity.setIsActive(floor.getIsActive());

    existingEntity.setModifiedUser(floor.getModifiedUser());
    existingEntity.setModifiedDate(floor.getModifiedDate());

    var saved = jpaRepository.save(existingEntity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public List<Floor> findAll() {
    return jpaRepository.findAll()
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }

}
