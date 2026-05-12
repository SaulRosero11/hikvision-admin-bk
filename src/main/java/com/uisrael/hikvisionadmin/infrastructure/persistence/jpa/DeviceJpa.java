package com.uisrael.hikvisionadmin.infrastructure.persistence.jpa;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.enums.DeviceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "devices")
public class DeviceJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_device")
  private Long id;

  @Column(name = "code", unique = true, nullable = false, length = 50)
  private String code;

  @Column(name = "ip", nullable = false, length = 100)
  private String ip;

  @Column(name = "port")
  private Integer port;

  @Column(name = "model", length = 100)
  private String model;

  @Column(name = "location", length = 255)
  private String location;

  // Hikvision Connection Fields
  @Column(name = "device_user", length = 100)
  private String deviceUser;

  @Column(name = "device_password", length = 255)
  private String devicePassword;

  @Column(name = "mac_address", length = 50)
  private String macAddress;

  @Column(name = "serial_number", length = 100)
  private String serialNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "device_type", nullable = false)
  private DeviceType deviceType;

  @Column(name = "firmware_version", length = 50)
  private String firmwareVersion;

  @Column(name = "is_enabled", nullable = false)
  private Boolean isEnabled;

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
  @JoinColumn(name = "floor_id", nullable = false)
  private FloorJpa floor;

}
