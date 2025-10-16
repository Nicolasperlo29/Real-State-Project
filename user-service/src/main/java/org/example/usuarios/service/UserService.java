package org.example.usuarios.service;

import org.example.usuarios.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> getUsers();

    UserEntity getUserByEmail(String email);

    UserEntity getUserById(Long id);
}
