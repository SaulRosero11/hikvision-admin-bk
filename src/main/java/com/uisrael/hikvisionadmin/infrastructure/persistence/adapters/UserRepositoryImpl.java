package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.User;
import com.uisrael.hikvisionadmin.domain.repositories.IUserRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserJpa;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IUserJpaMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

  private final IUserJpaRepository jpaRepository;
  private final IUserJpaMapper entityMapper;

  @Override
  public User save(User user) {
    UserJpa entity = entityMapper.toEntity(user);

    // Manual tie-up to ensure bidirectional relationship is set
    if (entity.getImages() != null) {
      entity.getImages().forEach(image -> image.setUser(entity));
    }
    if (entity.getFloorPermissions() != null) {
      entity.getFloorPermissions().forEach(permission -> permission.setUser(entity));
    }

    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<User> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public Optional<User> findByIdentification(String identification) {
    return jpaRepository.findByIdentification(identification).map(entityMapper::toDomain);
  }

  @Override
  public List<User> findAll() {
    return jpaRepository.findAll().stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<User> findByBuildingId(Long buildingId) {
    return jpaRepository.findByBuildingId(buildingId).stream()
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
