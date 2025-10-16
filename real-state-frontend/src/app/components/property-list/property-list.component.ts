import { Component, OnInit } from '@angular/core';
import { Property } from '../../interfaces/property';
import { PropertyService } from '../../services/property.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-property-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './property-list.component.html',
  styleUrls: ['./property-list.component.css', '../../styles/card.css']
})
export class PropertyListComponent implements OnInit {

  properties: Property[] = [];

  constructor(private propertyService: PropertyService) {

  }

  ngOnInit(): void {
    this.fetchProperties();
  }

  fetchProperties() {
    this.propertyService.getAllProperties().subscribe({
      next: (data) => {
        this.properties = data;
        console.log('Fetched properties:', data);
      }
      , error: (err) => {
        console.error('Error fetching properties', err);
      }
    });
  }

  nextImage(property: any) {
    if (!property.currentImageIndex) property.currentImageIndex = 0;
    property.currentImageIndex = (property.currentImageIndex + 1) % property.images.length;
  }

  prevImage(property: any) {
    if (!property.currentImageIndex) property.currentImageIndex = 0;
    property.currentImageIndex =
      (property.currentImageIndex - 1 + property.images.length) % property.images.length;
  }

}
