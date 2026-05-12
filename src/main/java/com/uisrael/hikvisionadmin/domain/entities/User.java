package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.uisrael.hikvisionadmin.domain.enums.Gender;
import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

  private final Long id;
  private final String identification;
  private final String fullName;
  private final Long buildingId;

  private final Gender gender;
  private final Integer roomNumber;
  private final Integer floorNumber;
  private final String doorRight;
  private final Boolean localUiRight;
  private final AccessValidity validity;

  private final List<UserImage> images;
  private final List<UserFloorPermission> floorPermissions;

  private final Boolean isActive;
  private final String createdUser;
  private final LocalDateTime createdDate;
  private final String modifiedUser;
  private final LocalDateTime modifiedDate;

  public void validate() {
    if (fullName == null || fullName.trim().isEmpty()) {
      throw new DomainException("User full name is required.");
    }
    if (identification == null || identification.trim().isEmpty()) {
      throw new DomainException("User identification is required.");
    }
    if (buildingId == null) {
      throw new DomainException("Building assignment is required.");
    }
  }
}
