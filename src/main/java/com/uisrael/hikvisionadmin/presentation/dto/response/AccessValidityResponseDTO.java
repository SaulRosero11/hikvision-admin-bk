package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccessValidityResponseDTO {
  private Boolean isEnabled;
  private LocalDateTime beginTime;
  private LocalDateTime endTime;
  private String timeType;
}
