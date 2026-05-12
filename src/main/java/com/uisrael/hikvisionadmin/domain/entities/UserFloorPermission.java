package com.uisrael.hikvisionadmin.domain.entities;

import java.time.LocalDateTime;

import com.uisrael.hikvisionadmin.domain.exceptions.DomainException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserFloorPermission {

  private final Long id;
  private final Long userId;
  private final Long floorId;
  private final Boolean accessAllowed;
  private final String remarks;

  private final Boolean isSynced;

  private final LocalDateTime assignedDate;
  private final LocalDateTime expirationDate;
  private final Boolean isActive;
  private final String createdUser;
  private final LocalDateTime createdDate;
  private final String modifiedUser;
  private final LocalDateTime modifiedDate;

  public void validate() {
    if (userId == null) {
      throw new DomainException("User reference is required for permission.");
    }
    if (floorId == null) {
      throw new DomainException("Floor reference is required for permission.");
    }
  }

}
