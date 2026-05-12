package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.enums.DeviceType;

import lombok.Data;

@Data
public class DeviceResponseDTO {

  private Long id;
  private String code;
  private String ip;
  private Integer port;
  private String model;
  private String location;
  private String deviceUser;
  private String devicePassword;
  private String macAddress;
  private String serialNumber;
  private DeviceType deviceType;
  private String firmwareVersion;
  private Boolean isEnabled;
  private Long floorId;
  private Boolean isActive;

  private String createdUser;
  private LocalDateTime createdDate;
  private String modifiedUser;
  private LocalDateTime modifiedDate;
}
