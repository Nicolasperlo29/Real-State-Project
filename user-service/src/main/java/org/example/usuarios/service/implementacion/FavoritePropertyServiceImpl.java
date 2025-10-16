package org.example.usuarios.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.usuarios.DTOS.FavoritePropertyRequest;
import org.example.usuarios.entity.FavoriteProperty;
import org.example.usuarios.repository.FavoritePropertyRepository;
import org.example.usuarios.service.FavoritePropertyService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritePropertyServiceImpl implements FavoritePropertyService {

    private final FavoritePropertyRepository favoritePropertyRepository;

    @Override
    public FavoriteProperty addFavoriteProperty(FavoritePropertyRequest request) {

        // Evitar duplicados
        if (favoritePropertyRepository.existsByUserIdAndPropertyId(request.getUserId(), request.getPropertyId())) {
            throw new RuntimeException("Property already favorited by this user");
        }

        FavoriteProperty favorite = FavoriteProperty.builder()
                .userId(request.getUserId())
                .propertyId(request.getPropertyId())
                .createdAt(LocalDateTime.now())
                .build();

        return favoritePropertyRepository.save(favorite);
    }

    @Override
    public void removeFavoriteProperty(Long userId, Long propertyId) {
        favoritePropertyRepository.findByUserIdAndPropertyId(userId, propertyId)
                .ifPresent(favoritePropertyRepository::delete);
    }

    @Override
    public List<FavoriteProperty> getFavoritesByUserId(Long userId) {
        return favoritePropertyRepository.findAllByUserId(userId);
    }
}
