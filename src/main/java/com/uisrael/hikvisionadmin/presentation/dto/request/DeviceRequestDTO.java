package com.uisrael.hikvisionadmin.presentation.dto.request;

import com.uisrael.hikvisionadmin.domain.enums.DeviceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceRequestDTO {
  @NotBlank(message = "Device code is required")
  private String code;

  @NotBlank(message = "IP address or URL is required")
  private String ip;

  private Integer port;
  private String location;

  @NotBlank(message = "Device user is required")
  private String deviceUser;

  @NotBlank(message = "Device password is required")
  private String devicePassword;

  @NotNull(message = "Device type is required")
  private DeviceType deviceType;

  // Campos opcionales: se auto-completan desde deviceInfo si no se envían
  private String model;
  private String macAddress;
  private String serialNumber;
  private String firmwareVersion;

  private Boolean isEnabled;
  private Boolean isActive;
}
