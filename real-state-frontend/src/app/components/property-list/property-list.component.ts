import { Component, OnInit } from '@angular/core';
import { Property } from '../../interfaces/property';
import { PropertyService } from '../../services/property.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { PropertyFavoriteService } from '../../services/property-favorite.service';
import { FavoritePropertyRequest } from '../../interfaces/favorite-property-request';

@Component({
  selector: 'app-property-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './property-list.component.html',
  styleUrls: ['./property-list.component.css', '../../styles/card.css']
})
export class PropertyListComponent implements OnInit {

  properties: Property[] = [];
  loggedIn = false;
  userId: number = 0;
  request: FavoritePropertyRequest = {
    propertyId: 0,
    userId: 0
  }

  constructor(private propertyService: PropertyService, private userService: UserService, private propertyFavoriteService: PropertyFavoriteService) {

  }

  ngOnInit(): void {
    this.fetchProperties();
    this.loggedIn = this.userService.isLoggedIn();
    this.userService.getProfile().subscribe({
      next: (data) => {
        this.userId = data.id;
      },
      error(err) {
        console.log('Error: ', err)
      }
    })
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

  addToFavorites(propertyId: number, userId: number) {
    this.request.userId = userId;
    this.request.propertyId = propertyId;

    this.propertyFavoriteService.addFavorite(this.request).subscribe({
      next: (data) => {
        console.log('Agregado: ', data)
      },
      error: (err) => {
        console.log('Error: ', err)
      }
    })
  }
}
