package org.example.usuarios.DTOS;

import lombok.Data;

@Data
public class FavoritePropertyRequest {

    private Long userId;
    private Long propertyId;
}
