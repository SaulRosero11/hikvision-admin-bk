package com.uisrael.hikvisionadmin.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "building")
public class BuildingJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_building")
  private Long id;

  @Column(name = "name", length = 150, nullable = false)
  private String name;

  @Column(name = "address", length = 255)
  private String address;

  @Column(name = "number_of_floors", nullable = false)
  private Integer numberOfFloors;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "created_user", nullable = false)
  private String createdUser;

  @Column(name = "created_date", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "modified_user", nullable = true)
  private String modifiedUser;

  @Column(name = "modified_date", nullable = true)
  private LocalDateTime modifiedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  private AdminJpa admin;

  @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<FloorJpa> floors;

}
