import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
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
  styleUrl: './home.component.css',
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
  isScrolled = false;
  menuOpen = false;
  loadingHorarios = false;

  readonly FALLBACK_IMAGE =
    'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80';

  stats = [
    { value: '+500', label: 'Propiedades publicadas' },
    { value: '+300', label: 'Clientes satisfechos' },
    { value: '5+', label: 'Años de experiencia' },
    { value: '98%', label: 'Tasa de satisfacción' },
  ];

  services = [
    {
      icon: '🏡',
      title: 'Compra y venta',
      desc: 'Asesoramiento integral para operaciones inmobiliarias.',
    },
    {
      icon: '📊',
      title: 'Tasaciones',
      desc: 'Valuaciones profesionales basadas en mercado real.',
    },
    {
      icon: '📋',
      title: 'Alquileres',
      desc: 'Gestión completa de alquileres y contratos.',
    },
    {
      icon: '💼',
      title: 'Inversiones',
      desc: 'Oportunidades inmobiliarias con alto potencial.',
    },
  ];

  testimonials = [
    {
      text: 'Excelente atención y acompañamiento durante todo el proceso.',
      name: 'María González',
      role: 'Compradora',
      initials: 'MG',
      color: '#c9a96e',
    },
    {
      text: 'Vendimos nuestra propiedad rápidamente y al precio que esperábamos.',
      name: 'Juan Pérez',
      role: 'Vendedor',
      initials: 'JP',
      color: '#8b9e7e',
    },
    {
      text: 'El equipo de Aureum superó todas mis expectativas. Muy recomendable.',
      name: 'Laura Sánchez',
      role: 'Compradora',
      initials: 'LS',
      color: '#7a8ba8',
    },
  ];

  metrics = [
    { value: '+500', label: 'operaciones realizadas' },
    { value: '+100M', label: 'en propiedades gestionadas' },
    { value: '24hs', label: 'tiempo de respuesta' },
  ];

  constructor(
    private propertyService: PropertyService,
    private ubicacionService: UbicacionService,
    private router: Router,
    private appointmentService: AppointmentService,
  ) {}

  // ── Scroll listener para navbar ──────────────────────────────────────────
  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 60;
  }

  // ── Cerrar con Escape ─────────────────────────────────────────────────────
  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.modal) this.cerrarModal();
  }

  ngOnInit(): void {
    this.today = new Date().toISOString().split('T')[0];

    this.propertyService.getAllProperties().subscribe((data) => {
      this.properties = data.slice(0, 3);
    });

    this.ubicacionService.getCiudades(this.pais).subscribe((response) => {
      this.ciudades = response.data;
    });
  }

  // ── Helpers ───────────────────────────────────────────────────────────────
  getPropertyImage(property: Property): string {
    return property.images?.[0]?.url ?? this.FALLBACK_IMAGE;
  }

  buscar(): void {
    const extras = this.ciudadSeleccionada
      ? { queryParams: { ubicacion: this.ciudadSeleccionada } }
      : {};
    this.router.navigate(['/list'], extras);
  }

  scrollToSection(id: string): void {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  // ── Modal ─────────────────────────────────────────────────────────────────
  contactar(): void {
    this.modal = true;
    // Bloquear scroll del body mientras el modal está abierto
    document.body.style.overflow = 'hidden';
  }

  cerrarModal(): void {
    this.modal = false;
    this.asunto = '';
    this.fecha = '';
    this.hora = '';
    this.horariosDisponibles = [];
    document.body.style.overflow = '';
  }

  cerrarModalPorBackdrop(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-backdrop')) {
      this.cerrarModal();
    }
  }

  cargarHorariosDisponibles(): void {
    if (!this.fecha) return;

    this.loadingHorarios = true;
    this.hora = '';
    this.horariosDisponibles = [];

    this.appointmentService.getAppointmentsByDate(this.fecha).subscribe({
      next: (reservas) => {
        const HORA_INICIO = 9;
        const HORA_FIN = 18;
        const SLOT = 60; // minutos

        const getMinutes = (dateTimeStr: string): number => {
          const [, time] = dateTimeStr.split('T');
          const [h, m] = time.split(':').map(Number);
          return h * 60 + m;
        };

        const ocupados = reservas.map((r) => {
          const start = getMinutes(r.dateTime);
          return { start, end: start + (r.durationMinutes ?? SLOT) };
        });

        for (let h = HORA_INICIO; h < HORA_FIN; h++) {
          const slotStart = h * 60;
          const slotEnd = slotStart + SLOT;
          const libre = !ocupados.some(
            (o) => slotStart < o.end && slotEnd > o.start,
          );
          if (libre) {
            this.horariosDisponibles.push(`${String(h).padStart(2, '0')}:00`);
          }
        }

        this.hora = this.horariosDisponibles[0] ?? '';

        if (!this.horariosDisponibles.length) {
          alert('No quedan horarios disponibles para este día.');
        }
      },
      error: () =>
        alert('No se pudieron cargar los horarios. Intentá de nuevo.'),
      complete: () => {
        this.loadingHorarios = false;
      },
    });
  }

  agendarConsulta(): void {
    if (!this.fecha || !this.hora) return;

    const appointment = {
      clientId: 1, // reemplazar con ID real del usuario autenticado
      propertyId: null,
      type: 'CONSULTATION' as const,
      dateTime: `${this.fecha}T${this.hora}:00`,
    };

    this.appointmentService.createAppointment(appointment).subscribe({
      next: () => {
        alert('¡Consulta agendada correctamente!');
        this.cerrarModal();
      },
      error: (err) => {
        console.error(err);
        alert('Error al agendar la consulta. Intentá nuevamente.');
      },
    });
  }
}
