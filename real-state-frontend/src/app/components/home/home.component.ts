import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Property } from '../../interfaces/property';
import { PropertyService } from '../../services/property.service';
import { UbicacionService } from '../../services/ubicacion.service';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../services/appointment.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  properties: Property[] = [];
  ciudades: string[] = [];
  pais: string = 'Argentina';
  ciudadSeleccionada: string = '';
  modal: boolean = false;
  today: string = '';
  asunto: string = '';
  fecha: string = '';
  hora: string = '';
  horariosDisponibles: string[] = [];

  constructor(private propertyService: PropertyService, private ubicacionService: UbicacionService, private router: Router, private appointmentService: AppointmentService) { }

  ngOnInit(): void {
    const now = new Date();
    this.today = now.toISOString().split('T')[0]; // formato yyyy-mm-dd
    this.propertyService.getAllProperties().subscribe(data => {
      this.properties = data.slice(0, 3);
    });

    this.ubicacionService.getCiudades(this.pais).subscribe(response => {
      this.ciudades = response.data;
    });
  }

  buscar(): void {
    if (this.ciudadSeleccionada) {
      this.router.navigate(['/list'], { queryParams: { ubicacion: this.ciudadSeleccionada } });
    } else {
      this.router.navigate(['/list']); // sin filtro
    }
  }

  contactar() {
    this.modal = true;
    this.cargarHorariosDisponibles();
  }

  cerrarModal() {
    this.modal = false;
    this.asunto = '';
    this.fecha = '';
    this.hora = '';
  }

  cargarHorariosDisponibles() {
    if (!this.fecha) return;

    this.appointmentService.getAppointmentsByDate(this.fecha).subscribe(reservas => {
      console.log('Reservas: ', reservas);

      const horarioInicio = 9; // 09:00
      const horarioFin = 18;    // 18:00
      const SLOT = 60;          // duración de cada consulta en minutos (slot fijo)

      function getLocalHour(dateTimeStr: string): number {
        const [date, time] = dateTimeStr.split('T');
        const [hour, minute] = time.split(':').map(x => parseInt(x, 10));
        return hour;
      }

      const ocupados: { start: number, end: number }[] = reservas.map(r => {
        const start = getLocalHour(r.dateTime) * 60;
        const duration = r.durationMinutes ?? SLOT;
        return { start, end: start + duration };
      });

      this.horariosDisponibles = [];

      for (let h = horarioInicio; h <= horarioFin - 1; h++) {
        const slotStart = h * 60;
        const slotEnd = slotStart + SLOT;

        // Verificar si el slot se superpone con alguna reserva
        const overlap = ocupados.some(o => slotStart < o.end && slotEnd > o.start);

        if (!overlap) {
          const horaStr = h.toString().padStart(2, '0') + ':00';
          this.horariosDisponibles.push(horaStr);
        }
      }

      if (this.horariosDisponibles.length === 0) {
        alert('No quedan horarios disponibles para este día.');
        this.hora = '';
      } else {
        this.hora = this.horariosDisponibles[0];
      }
    });
  }


  agendarConsulta() {
    // yyyy-MM-ddTHH:mm:ss sin Z ni offset
    const dateTime = `${this.fecha}T${this.hora}:00`;

    const appointment = {
      clientId: 1, // reemplazar con ID real
      propertyId: null,
      type: 'CONSULTATION' as 'CONSULTATION',
      dateTime: dateTime
    };

    this.appointmentService.createAppointment(appointment).subscribe({
      next: (res) => {
        alert('Consulta agendada correctamente!');
        this.cerrarModal();
      },
      error: (err) => {
        console.error(err);
        alert('Error al agendar la consulta.');
      }
    });
  }

}
