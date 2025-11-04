package org.example.agendaservice.model;

public enum AppointmentStatus {
    PENDING,      // Cliente la crea, pendiente de confirmación
    CONFIRMED,    // Aprobada por el agente o sistema
    CANCELLED,    // Cancelada por alguna de las partes
    COMPLETED     // Realizada
}
