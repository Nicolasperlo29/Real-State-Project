package org.example.usuarios.service;

import org.example.usuarios.DTOS.AuthResponse;
import org.example.usuarios.DTOS.LoginRequest;
import org.example.usuarios.DTOS.UserDTO;
import org.example.usuarios.DTOS.UserRequest;
import org.example.usuarios.entity.AuthUserEntity;

import java.util.List;

public interface AuthService {

    UserDTO register(UserRequest request);

    public AuthResponse login(LoginRequest request);

    List<AuthUserEntity> getUsers();

    AuthUserEntity getUserByEmail(String email);

    AuthUserEntity getUserById(Long id);
}
