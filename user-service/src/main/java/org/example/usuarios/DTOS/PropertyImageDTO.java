package org.example.usuarios.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class PropertyImageDTO {

    private Long id;

    private String url;
}
