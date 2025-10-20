package org.example.usuarios.controller;

import jakarta.validation.Valid;
import org.example.usuarios.DTOS.*;
import org.example.usuarios.entity.FavoriteProperty;
import org.example.usuarios.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.example.usuarios.service.UserService;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserEntity> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity entity = userService.getUserById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/{userId}/profile")
    public Mono<UserWithFavoritesDTO> getUserWithFavorites(@PathVariable Long userId) {
        // Llamamos directamente al método reactivo del servicio
        return userService.getUserWithFavoritesReactive(userId);
    }

}
