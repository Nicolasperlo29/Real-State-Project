package org.example.usuarios.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.usuarios.DTOS.*;
import org.example.usuarios.domain.Role;
import org.example.usuarios.entity.UserEntity;
import org.example.usuarios.exception.EmailAlreadyExistsException;
import org.example.usuarios.exception.InvalidEmailException;
import org.example.usuarios.exception.InvalidPasswordException;
import org.example.usuarios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.example.usuarios.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getUsers() {    
        List<UserEntity> users = userRepository.findAll();

        return users;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public UserEntity getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException("Usuario no encontrado con Id: " + id));
    }
}
