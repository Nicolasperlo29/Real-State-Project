import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Appointment } from '../interfaces/appointment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private baseUrl = 'http://localhost:8085/appointments'; // URL de tu backend

  constructor(private http: HttpClient) { }

  // Crear una reserva
  createAppointment(appointment: Appointment): Observable<Appointment> {
    return this.http.post<Appointment>(this.baseUrl, appointment);
  }

  // Listar reservas por cliente
  getAppointmentsByClient(clientId: number): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/client/${clientId}`);
  }

  // Listar reservas por agente
  getAppointmentsByAgent(agentId: number): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/agent/${agentId}`);
  }

  // Listar reservas por fecha
  getAppointmentsByDate(fecha: string): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/date/${fecha}`);
  }

  // Actualizar estado de la reserva
  // updateStatus(id: number, status: Appointment['status']): Observable<Appointment> {
  //   return this.http.put<Appointment>(`${this.baseUrl}/${id}/status?status=${status}`, {});
  // }
}
