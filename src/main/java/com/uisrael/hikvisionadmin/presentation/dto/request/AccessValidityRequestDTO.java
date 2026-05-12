package com.uisrael.hikvisionadmin.presentation.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessValidityRequestDTO {

  @NotNull(message = "Enabled status for validity is required")
  private Boolean isEnabled;

  @NotNull(message = "Begin time is required")
  private LocalDateTime beginTime;

  @NotNull(message = "End time is required")
  private LocalDateTime endTime;

  @NotNull(message = "Time type (LOCAL/UTC) is required")
  private String timeType;
}
