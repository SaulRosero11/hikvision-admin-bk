package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.enums.TimeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccessValidity {
  private final Boolean isEnabled;
  private final LocalDateTime beginTime;
  private final LocalDateTime endTime;
  private final TimeType timeType;

  public boolean isCurrentlyValid() {
    if (!isEnabled)
      return false;
    LocalDateTime now = LocalDateTime.now();
    return now.isAfter(beginTime) && now.isBefore(endTime);
  }
}
