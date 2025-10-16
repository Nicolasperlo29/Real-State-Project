package org.example.proyectogestionstock.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private String city;
    private String address;
    private Double price;
    private Double areaCubierta; // en m2
    private Double areaTotal;
    private Integer rooms;
    private Integer bathrooms;
    private String type; // Casa, Departamento, etc.
    private String status; // En venta, En alquiler
    private Double latitude;
    private Double longitude;
    private Long userId;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PropertyImage> images = new ArrayList<>();
}
