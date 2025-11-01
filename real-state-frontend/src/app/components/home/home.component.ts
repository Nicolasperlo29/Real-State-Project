import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Property } from '../../interfaces/property';
import { PropertyService } from '../../services/property.service';
import { UbicacionService } from '../../services/ubicacion.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  properties: Property[] = [];
  ciudades: string[] = [];
  pais: string = 'Argentina';
  ciudadSeleccionada: string = '';

  constructor(private propertyService: PropertyService, private ubicacionService: UbicacionService, private router: Router) { }

  ngOnInit(): void {
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
}
