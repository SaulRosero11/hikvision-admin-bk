package com.uisrael.hikvisionadmin.application.usecase.entry;

import com.uisrael.hikvisionadmin.presentation.dto.request.AdminRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.request.LoginRequestDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.AdminResponseDTO;
import com.uisrael.hikvisionadmin.presentation.dto.response.LoginResponseDTO;

public interface IAuthUseCase {

  LoginResponseDTO login(LoginRequestDTO request);

  AdminResponseDTO registerAdmin(AdminRequestDTO request);

  AdminResponseDTO getNowAdmin(String username);

  void changePassword(String username, String passwordActual, String passwordNuevo);

}
