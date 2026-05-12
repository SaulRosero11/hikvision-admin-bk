package com.uisrael.hikvisionadmin.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;

import com.uisrael.hikvisionadmin.domain.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_user")
  private Long id;

  @Column(unique = true, nullable = false, length = 20)
  private String identification;

  @Column(name = "full_name", nullable = false, length = 200)
  private String fullName;

  @Column(name = "building_id", nullable = false)
  private Long buildingId;

  // Hikvision Fields
  @Enumerated(EnumType.STRING)
  private Gender gender;

  private Integer roomNumber;
  private Integer floorNumber;
  private String doorRight;
  private Boolean localUiRight;

  @Embedded
  private AccessValidityEmbeddable validity;

  // Relationships
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<UserImageJpa> images;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<UserFloorPermissionJpa> floorPermissions;

  private Boolean isActive;
  private String createdUser;
  private LocalDateTime createdDate;
  private String modifiedUser;
  private LocalDateTime modifiedDate;
}
