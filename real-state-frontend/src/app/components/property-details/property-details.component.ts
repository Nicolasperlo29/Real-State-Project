import { Component, OnInit } from '@angular/core';
import { Property } from '../../interfaces/property';
import { ActivatedRoute } from '@angular/router';
import { PropertyService } from '../../services/property.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MapComponent } from '../map/map.component';

@Component({
  selector: 'app-property-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './property-details.component.html',
  styleUrls: ['./property-details.component.css']
})
export class PropertyDetailsComponent implements OnInit {

  property: Property = {
    id: 0,
    title: '',
    description: '',
    city: '',
    address: '',
    price: 0,
    areaCubierta: 0,
    areaTotal: 0,
    rooms: 0,
    bathrooms: 0,
    type: '',
    status: '',
    latitude: 0,
    longitude: 0,
    images: [],
    currentImageIndex: 0
  };
  currentImage = 0;

  isFavorite = false;

  contactData = {
    name: '',
    email: '',
    phone: '',
    message: ''
  };

  constructor(
    private route: ActivatedRoute,
    private propertyService: PropertyService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProperty(id);
  }

  loadProperty(id: number) {
    this.propertyService.getPropertyById(id).subscribe({
      next: (data) => {
        this.property = data;
        console.log('Property details:', data);
      }
      , error: (err) => {
        console.error('Error fetching property details', err);
      }
    });
  }

  formatPrice(price: number) {
    return new Intl.NumberFormat('es-AR', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0
    }).format(price);
  }

  handleSubmit() {
    alert('Consulta enviada correctamente');
    this.contactData = { name: '', email: '', phone: '', message: '' };
  }

  prevImage() {
    if (!this.property.images?.length) return;
    this.currentImage = (this.currentImage - 1 + this.property.images.length) % this.property.images.length;
  }

  nextImage() {
    if (!this.property.images?.length) return;
    this.currentImage = (this.currentImage + 1) % this.property.images.length;
  }
}
