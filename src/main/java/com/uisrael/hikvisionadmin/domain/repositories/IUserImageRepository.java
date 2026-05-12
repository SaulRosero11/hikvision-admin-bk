package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.UserImage;

public interface IUserImageRepository {

  UserImage save(UserImage userImage);

  Optional<UserImage> findById(Long id);

  List<UserImage> findByUserId(Long userId);

  void deleteById(Long id);

  void deleteByUserId(Long userId);
}
