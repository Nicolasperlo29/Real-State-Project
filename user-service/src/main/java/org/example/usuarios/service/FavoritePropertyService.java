package org.example.usuarios.service;

import org.example.usuarios.DTOS.FavoritePropertyRequest;
import org.example.usuarios.entity.FavoriteProperty;

import java.util.List;

public interface FavoritePropertyService {

    FavoriteProperty addFavoriteProperty(FavoritePropertyRequest request);

    public void removeFavoriteProperty(Long userId, Long propertyId);

    public List<FavoriteProperty> getFavoritesByUserId(Long userId);
}
