package com.uisrael.hikvisionadmin.infrastructure.persistence.jpa;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.enums.TimeType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Embeddable
public class AccessValidityEmbeddable {

  @Column(name = "validity_enabled")
  private Boolean isEnabled;

  @Column(name = "validity_begin_time")
  private LocalDateTime beginTime;

  @Column(name = "validity_end_time")
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "validity_time_type", length = 20)
  private TimeType timeType;
}
