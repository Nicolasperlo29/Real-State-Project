package org.example.usuarios.controller;

import jakarta.validation.Valid;
import org.example.usuarios.DTOS.*;
import org.example.usuarios.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.example.usuarios.service.UserService;

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

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity entity = userService.getUserByEmail(email);
        return ResponseEntity.ok(entity);
    }
}
