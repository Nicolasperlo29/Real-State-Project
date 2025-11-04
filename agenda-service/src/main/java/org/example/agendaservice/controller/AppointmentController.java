package org.example.agendaservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.agendaservice.model.Appointment;
import org.example.agendaservice.model.AppointmentStatus;
import org.example.agendaservice.service.implementation.AppointmentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServiceImpl appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClient(agentId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClient(clientId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status
    ) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @GetMapping("/date/{fecha}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(@PathVariable String fecha) {
        LocalDateTime start = LocalDateTime.parse(fecha + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(fecha + "T23:59:59");
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(start, end));
    }

}
