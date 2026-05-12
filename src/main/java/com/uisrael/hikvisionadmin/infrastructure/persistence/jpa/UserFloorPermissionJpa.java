package com.uisrael.hikvisionadmin.infrastructure.persistence.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "user_floor_permissions")
public class UserFloorPermissionJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_permission")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_user"))
  private UserJpa user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "floor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_floor"))
  private FloorJpa floor;

  @Column(name = "access_allowed")
  private Boolean accessAllowed;

  private String remarks;

  @Column(name = "is_synced")
  private Boolean isSynced;

  private LocalDateTime assignedDate;
  private LocalDateTime expirationDate;

  private Boolean isActive;
  private String createdUser;
  private LocalDateTime createdDate;
  private String modifiedUser;
  private LocalDateTime modifiedDate;
}
