package org.example.usuarios.service;

import org.example.usuarios.DTOS.UserWithFavoritesDTO;
import org.example.usuarios.entity.UserEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {

    public Mono<UserWithFavoritesDTO> getUserWithFavoritesReactive(Long userId);

    List<UserEntity> getUsers();

    UserEntity getUserByEmail(String email);

    UserEntity getUserById(Long id);
}
