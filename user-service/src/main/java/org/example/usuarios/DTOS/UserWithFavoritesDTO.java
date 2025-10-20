package org.example.usuarios.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithFavoritesDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String profileImageUrl;
    private List<FavoritePropertyDTO> favorites;
}
