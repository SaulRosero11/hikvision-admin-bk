package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.enums.DeviceType;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Device {

  private final Long id;
  private final String code;
  private final String ip;
  private final Integer port;
  private final String model;
  private final String location;

  // Hikvision Connection Fields
  private final String deviceUser;
  private final String devicePassword;
  private final String macAddress;
  private final String serialNumber;
  private final DeviceType deviceType;
  private final String firmwareVersion;
  private final Boolean isEnabled;

  // Relationship with Floor
  private final Long floorId;

  // Audit Fields
  private final Boolean isActive;
  private final String createdUser;
  private final LocalDateTime createdDate;
  private final String modifiedUser;
  private final LocalDateTime modifiedDate;

  public void validate() {
    if (code == null || code.trim().isEmpty()) {
      throw new DomainException("Device code is required.");
    }
    if (ip == null || ip.trim().isEmpty()) {
      throw new DomainException("Device IP or URL is required.");
    }

    boolean isUrl = ip.startsWith("http://") || ip.startsWith("https://");
    if (!isUrl && (port == null || port < 1 || port > 65535)) {
      throw new DomainException("Device port must be between 1 and 65535.");
    }

    if (floorId == null) {
      throw new DomainException("Floor assignment is required.");
    }

    if (isActive == null) {
      throw new DomainException("Device active status is required.");
    }
  }

}
