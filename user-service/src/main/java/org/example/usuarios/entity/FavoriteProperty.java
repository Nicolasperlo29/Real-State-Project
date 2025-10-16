package org.example.usuarios.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;      // ID del usuario
    private Long propertyId;    // ID de la propiedad en el otro microservicio

    private LocalDateTime createdAt;
}
