package org.example.usuarios.controller;

import org.example.usuarios.DTOS.FavoritePropertyRequest;
import org.example.usuarios.entity.FavoriteProperty;
import org.example.usuarios.service.FavoritePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritePropertyController {

    @Autowired
    private FavoritePropertyService favoritePropertyService;

    // Agregar una propiedad favorita
    @PostMapping
    public ResponseEntity<FavoriteProperty> addFavorite(@RequestBody FavoritePropertyRequest request) {
        FavoriteProperty favorite = favoritePropertyService.addFavoriteProperty(request);
        return ResponseEntity.ok(favorite);
    }

    // Eliminar una propiedad favorita
    @DeleteMapping("/{userId}/{propertyId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId, @PathVariable Long propertyId) {
        favoritePropertyService.removeFavoriteProperty(userId, propertyId);
        return ResponseEntity.noContent().build();
    }

    // Listar todas las propiedades favoritas de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteProperty>> getFavoritesByUser(@PathVariable Long userId) {
        List<FavoriteProperty> favorites = favoritePropertyService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(favorites);
    }
}
