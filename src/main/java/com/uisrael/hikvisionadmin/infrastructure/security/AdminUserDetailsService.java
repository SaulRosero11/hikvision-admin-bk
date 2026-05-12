package com.uisrael.hikvisionadmin.infrastructure.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.AdminJpa;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IAdminJpaRespository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final IAdminJpaRespository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminJpa admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado: " + username));

        return new AdminUserDetails(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getIsActive(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
