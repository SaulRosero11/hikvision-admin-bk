package com.uisrael.hikvisionadmin.infrastructure.configuration;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.AdminJpa;
import com.uisrael.hikvisionadmin.infrastructure.persistence.jpa.BuildingJpa;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IAdminJpaRespository;
import com.uisrael.hikvisionadmin.infrastructure.repositories.IBuildingJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IBuildingJpaRepository edificioRepository;
    private final IAdminJpaRespository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        if (administradorRepository.count() == 0) {
            log.info("Iniciando creación de datos por defecto...");

            AdminJpa admin = AdminJpa.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@sistema.com")
                    .fullName("Administrador del Sistema")
                    .isActive(true)
                    .createdDate(LocalDateTime.now())
                    .build();

            admin = administradorRepository.save(admin);
            log.info("Administrador por defecto creado.");

            if (edificioRepository.count() == 0) {
                log.info("Creando edificio asociado al administrador...");

                BuildingJpa edificio = BuildingJpa.builder()
                        .name("Edificio Principal")
                        .address("Dirección Principal")
                        .numberOfFloors(5)
                        .description("Edificio principal del sistema")
                        .phone("0000000000")
                        .isActive(true)
                        .createdDate(LocalDateTime.now())
                        .createdUser("SYSTEM")
                        .admin(admin)
                        .build();

                edificioRepository.save(edificio);
                log.info("Edificio principal asociado al admin con éxito.");
            }

            log.info("Configuración inicial completada. Login: admin / admin123");
        }
    }

}
