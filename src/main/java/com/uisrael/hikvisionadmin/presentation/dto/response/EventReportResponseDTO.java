package com.uisrael.hikvisionadmin.presentation.dto.response;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventReportResponseDTO {

  private String from;
  private String to;
  private int totalEvents;
  private int validCount;
  private int invalidCount;
  private Map<String, Integer> byVerifyMode;
}
