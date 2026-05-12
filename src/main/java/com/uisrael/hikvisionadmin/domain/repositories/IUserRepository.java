package com.uisrael.hikvisionadmin.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.uisrael.hikvisionadmin.domain.entities.User;

public interface IUserRepository {

  User save(User user);

  Optional<User> findById(Long id);

  Optional<User> findByIdentification(String identification);

  List<User> findAll();

  List<User> findByBuildingId(Long buildingId);

  void deleteById(Long id);

  boolean existsById(Long id);
}
