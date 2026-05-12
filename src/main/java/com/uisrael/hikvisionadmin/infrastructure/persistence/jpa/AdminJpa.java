package com.uisrael.hikvisionadmin.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "adminUser", uniqueConstraints = {
    @UniqueConstraint(name = "uk_admin_username", columnNames = { "username" }),
    @UniqueConstraint(name = "uk_admin_email", columnNames = ("email"))
})
public class AdminJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_admin")
  private Long id;

  @Column(name = "username", length = 50, nullable = false)
  private String username;

  @Column(name = "password", length = 255, nullable = false)
  private String password;

  @Column(name = "email", length = 100, nullable = false)
  private String email;

  @Column(name = "full_name", length = 150)
  private String fullName;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "created_date", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "modified_date", nullable = true)
  private LocalDateTime modifiedDate;

  @Column(name = "modified_user", nullable = true)
  private String modifiedUser;

  @Column(name = "last_access")
  private LocalDateTime lastAccess;

  @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
  @Builder.Default
  private List<BuildingJpa> buildings = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    if (createdDate == null) {
      createdDate = LocalDateTime.now();
    }
    if (isActive == null) {
      isActive = true;
    }
  }

}
