package org.example.agendaservice.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.agendaservice.model.Appointment;
import org.example.agendaservice.model.AppointmentStatus;
import org.example.agendaservice.model.AppointmentType;
import org.example.agendaservice.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl {
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setCreatedAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
    }

    private void validateAppointment(Appointment appointment) {

        // Validar tipo de reserva
        if (appointment.getType() == AppointmentType.PROPERTY_VISIT && appointment.getPropertyId() == null) {
            throw new IllegalArgumentException("La propiedad es obligatoria para una visita.");
        }

        if (appointment.getType() == AppointmentType.CONSULTATION) {
            appointment.setPropertyId(null);
        }

        // Validar horario laboral
        int hour = appointment.getDateTime().getHour();
        if (hour < 9 || hour > 17) { // última hora de inicio permitida
            throw new IllegalArgumentException("Las reservas solo pueden hacerse entre las 9 y las 18 hs.");
        }

        // Validar que no haya otra reserva en ese horario (considerando duración)
        LocalDateTime start = appointment.getDateTime();
        LocalDateTime end = start.plusMinutes(appointment.getDurationMinutes());

        if (appointmentRepository.existsByDateTimeBetween(start, end)) {
            throw new IllegalArgumentException("Ya hay una reserva en ese horario.");
        }

        // Limitar cantidad de reservas activas del cliente
        int activeCount = appointmentRepository.countByClientIdAndStatus(appointment.getClientId(), AppointmentStatus.PENDING);
        if (activeCount >= 3) {
            throw new IllegalArgumentException("Ya tenés 3 reservas activas. Cancelá alguna antes de crear una nueva.");
        }
    }

    public List<Appointment> getAppointmentsByClient(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada."));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDateTimeBetween(start, end);
    }

}
