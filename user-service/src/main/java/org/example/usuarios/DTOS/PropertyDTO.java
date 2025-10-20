package org.example.usuarios.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyDTO {
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
    private List<PropertyImageDTO> images;
}
