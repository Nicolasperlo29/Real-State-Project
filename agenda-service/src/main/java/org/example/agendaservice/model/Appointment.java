package org.example.agendaservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;  // ID del cliente (desde users-service)
//    private Long agentId;   // ID del agente
    private Long propertyId; // Puede ser null si es asesoramiento general

    @Enumerated(EnumType.STRING)
    private AppointmentType type; // CONSULTATION o PROPERTY_VISIT

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING, CONFIRMED, CANCELLED, COMPLETED

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime; // Fecha y hora de la reserva

    private int durationMinutes = 60; // duración en minutos

    private LocalDateTime createdAt = LocalDateTime.now();
}
