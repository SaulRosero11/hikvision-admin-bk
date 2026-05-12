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
@Table(name = "floors")
public class FloorJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_floor")
  private Long id;

  @Column(name = "floor_number", nullable = false)
  private Integer floorNumber;

  @Column(name = "name", length = 150, nullable = false)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "created_user", nullable = false)
  private String createdUser;

  @Column(name = "created_date", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "modified_user")
  private String modifiedUser;

  @Column(name = "modified_date")
  private LocalDateTime modifiedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "building_id", nullable = false)
  private BuildingJpa building;

  @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<DeviceJpa> devices;
}
