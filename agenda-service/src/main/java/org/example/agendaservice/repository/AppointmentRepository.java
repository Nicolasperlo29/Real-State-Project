package org.example.agendaservice.repository;

import org.example.agendaservice.model.Appointment;
import org.example.agendaservice.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Verificar si ya hay una reserva en ese rango de tiempo
    boolean existsByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // Contar reservas activas de un cliente
    int countByClientIdAndStatus(Long clientId, AppointmentStatus status);

    // Buscar citas por cliente
    List<Appointment> findByClientId(Long clientId);

    List<Appointment> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
