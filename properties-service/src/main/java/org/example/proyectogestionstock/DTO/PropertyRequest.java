package org.example.proyectogestionstock.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PropertyRequest {

    private String title;
    private String description;
    private String city;
    private String address;
    private Double price;
    private Double areaCubierta;
    private Double areaTotal;
    private Integer rooms;
    private Integer bathrooms;
    private String type;    // Casa, Departamento, etc.
    private String status;  // En venta, En alquiler
    private Double latitude;
    private Double longitude;
    private List<String> images;
    private Long userId;
}
