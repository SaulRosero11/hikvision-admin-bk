package com.uisrael.hikvisionadmin.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.UserImageJpa;

@Repository
public interface IUserImageJpaRepository extends JpaRepository<UserImageJpa, Long> {

  // Obtener las fotos de un usuario para enviar al hardware
  List<UserImageJpa> findByUserId(Long userId);

  // Limpiar imágenes de un usuario
  void deleteByUserId(Long userId);

}
