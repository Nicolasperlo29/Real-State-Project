package org.example.usuarios.controller;

import org.example.usuarios.DTOS.*;
import org.example.usuarios.entity.AuthUserEntity;
import org.example.usuarios.repository.AuthUserRepository;
import org.example.usuarios.service.implementacion.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.example.usuarios.service.AuthService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private JwtServiceImpl jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody UserRequest request) {
        UserDTO user = authService.register(request); // service lanza excepciones si hay error
        ApiResponse<UserDTO> response = new ApiResponse<>(true, "Usuario registrado correctamente", user);
        return ResponseEntity.ok(response);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<AuthUserEntity> getUsers() {
        return authService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthUserEntity> getUserById(@PathVariable Long id) {
        AuthUserEntity entity = authService.getUserById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        String email = authentication.getName();
        Optional<AuthUserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        AuthUserEntity user = userOptional.get();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "fullname", user.getFullname(),
                "email", user.getEmail()
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido o expirado");
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        AuthUserEntity user = authService.getUserByEmail(email);

        String newAccessToken = jwtService.generateToken(user, 1000 * 60 * 15); // 15 min

        return ResponseEntity.ok(Map.of("token", newAccessToken));
    }

}
