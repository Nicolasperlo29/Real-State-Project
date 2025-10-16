package org.example.usuarios.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.usuarios.DTOS.*;
import org.example.usuarios.domain.Role;
import org.example.usuarios.entity.AuthUserEntity;
import org.example.usuarios.event.UserCreatedEvent;
import org.example.usuarios.exception.*;
import org.example.usuarios.repository.AuthUserRepository;
import org.example.usuarios.service.AuthService;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthUserRepository userRepository;
    private final JwtServiceImpl jwtService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;
    @Override
    public UserDTO register(UserRequest request) {

        // Validación del email
        if (request.getFullname() == null || request.getFullname().isEmpty()) {
            throw new InvalidNameException("El nombre es obligatorio");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new InvalidEmailException("El email es obligatorio");
        }

        if (!request.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidEmailException("El email no es válido");
        }

        // Validación de la contraseña
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new InvalidPasswordException("La contraseña debe tener al menos 6 caracteres");
        }

        if (request.getAcceptTerms() == null || !request.getAcceptTerms()) {
            throw new InvalidTermsException("Debes aceptar los términos y condiciones");
        }

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        // Crear usuario
        AuthUserEntity user = new AuthUserEntity();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // asigna un rol por defecto
        user.setActive(true);
        user.setAcceptTerms(request.getAcceptTerms());

        // Guardar en la base
        AuthUserEntity savedUser = userRepository.save(user);


        UserCreatedEvent event = new UserCreatedEvent(
                savedUser.getFullname(),
                savedUser.getId(),
                savedUser.getEmail()
        );

//        rabbitTemplate.convertAndSend(exchange.getName(), "user.created", event);
        sendUserCreatedEvent(event); // se ejecuta en otro hilo

        // Retornar DTO
        return new UserDTO(savedUser.getId(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        AuthUserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.isActive()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
    @Override
    public List<AuthUserEntity> getUsers() {
        List<AuthUserEntity> users = userRepository.findAll();

        return users;
    }

    @Override
    public AuthUserEntity getUserByEmail(String email) {
        Optional<AuthUserEntity> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public AuthUserEntity getUserById(Long id) {
        Optional<AuthUserEntity> user = userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException("Usuario no encontrado con Id: " + id));
    }

    @Async
    public void sendUserCreatedEvent(UserCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange.getName(), "user.created", event);
    }

}
