package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Floor {

  private final Long id;
  private final Integer floorNumber;
  private final String name;
  private final String description;
  private final Long buildingId;

  private final Boolean isActive;
  private final String createdUser;
  private final LocalDateTime createdDate;
  private final String modifiedUser;
  private final LocalDateTime modifiedDate;

  public void validate() {
    if (floorNumber == null)
      throw new DomainException("Floor number is required.");
    if (buildingId == null)
      throw new DomainException("Building ID is required.");
  }

}
