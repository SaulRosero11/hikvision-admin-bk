package com.uisrael.hikvisionadmin.infrastructure.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class AdminUserDetails extends User {

  private final Long id;

  public AdminUserDetails(Long id, String username, String password, boolean enabled,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, true, true, true, authorities);
    this.id = id;
  }
}
