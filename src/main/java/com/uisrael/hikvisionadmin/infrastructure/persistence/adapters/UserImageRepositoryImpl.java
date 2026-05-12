package com.uisrael.hikvisionadmin.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.domain.entities.UserImage;
import com.uisrael.hikvisionadmin.domain.repositories.IUserImageRepository;
import com.uisrael.hikvisionadmin.infrastructure.persistence.mappers.IUserImageMapper;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IUserImageJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserImageRepositoryImpl implements IUserImageRepository {

  private final IUserImageJpaRepository jpaRepository;
  private final IUserImageMapper entityMapper;

  @Override
  public UserImage save(UserImage userImage) {
    var entity = entityMapper.toEntity(userImage);
    var saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<UserImage> findById(Long id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public List<UserImage> findByUserId(Long userId) {
    return jpaRepository.findByUserId(userId).stream()
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
}
