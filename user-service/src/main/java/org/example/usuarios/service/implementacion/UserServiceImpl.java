package org.example.usuarios.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.usuarios.DTOS.*;
import org.example.usuarios.domain.Role;
import org.example.usuarios.entity.FavoriteProperty;
import org.example.usuarios.entity.UserEntity;
import org.example.usuarios.exception.EmailAlreadyExistsException;
import org.example.usuarios.exception.InvalidEmailException;
import org.example.usuarios.exception.InvalidPasswordException;
import org.example.usuarios.repository.FavoritePropertyRepository;
import org.example.usuarios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.example.usuarios.repository.UserRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FavoritePropertyRepository favoritePropertyRepository;
    private final WebClient webClient;

    private final String PROPERTY_SERVICE_URL = "http://localhost:8083/properties"; // endpoint de propiedades

    @Override
    public Mono<UserWithFavoritesDTO> getUserWithFavoritesReactive(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<FavoriteProperty> favoriteEntities = favoritePropertyRepository.findByUserId(userId);

        // Si no hay favoritos, devolvemos un Mono con lista vacía
        if (favoriteEntities.isEmpty()) {
            return Mono.just(new UserWithFavoritesDTO(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getProfileImageUrl(),
                    List.of() // lista vacía, nunca null
            ));
        }

        List<Mono<FavoritePropertyDTO>> favoriteMonos = favoriteEntities.stream()
                .map(fav -> webClient.get()
                        .uri(PROPERTY_SERVICE_URL + "/" + fav.getPropertyId())
                        .retrieve()
                        .bodyToMono(PropertyDTO.class)
                        .map(property -> new FavoritePropertyDTO(fav.getPropertyId(), property))
                        .onErrorResume(e -> Mono.just(new FavoritePropertyDTO(fav.getPropertyId(), null)))
                )
                .collect(Collectors.toList());

        return Mono.zip(favoriteMonos, results -> {
            List<FavoritePropertyDTO> favorites =
                    java.util.Arrays.stream(results)
                            .map(obj -> (FavoritePropertyDTO) obj)
                            .collect(Collectors.toList());

            return new UserWithFavoritesDTO(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getProfileImageUrl(),
                    favorites
            );
        });
    }

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
