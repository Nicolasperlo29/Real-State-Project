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
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "X-User-Id", required = false) String userEmail,
            @RequestHeader(value = "X-User-Role", required = false) String userRole) {

        System.out.println("=== /auth/me ===");
        System.out.println("X-User-Id: " + userEmail);
        System.out.println("X-User-Role: " + userRole);

        if (userEmail == null || userEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No autenticado - falta header X-User-Id");
        }

        Optional<AuthUserEntity> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        AuthUserEntity user = userOptional.get();

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole()
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
