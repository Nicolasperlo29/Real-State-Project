import { Component, OnInit } from '@angular/core';
import { Property } from '../../interfaces/property';
import { PropertyService } from '../../services/property.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { PropertyFavoriteService } from '../../services/property-favorite.service';
import { FavoritePropertyRequest } from '../../interfaces/favorite-property-request';
import { UsuariosService } from '../../services/usuarios.service';
import { UserWithFavorites } from '../../interfaces/user-with-favorites';

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
  request: FavoritePropertyRequest = { propertyId: 0, userId: 0 };

  usuarioCompleto?: UserWithFavorites;
  isLoading = true;

  // Filtros
  selectedType: string = 'todos';
  selectedSort: string = 'reciente';
  minPrice: number = 0;
  maxPrice: number = 0;
  searchTerm: string = '';
  ubicacion: string = '';

  // Tipos únicos
  propertyTypes: string[] = [];

  constructor(
    private propertyService: PropertyService,
    private userService: UserService,
    private propertyFavoriteService: PropertyFavoriteService,
    private usuarioService: UsuariosService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.loggedIn = this.userService.isLoggedIn();

    // Suscribirse a queryParams
    this.route.queryParams.subscribe(params => {
      this.ubicacion = params['ubicacion'] || '';
      this.fetchProperties();
    });

    this.userService.getProfile().subscribe({
      next: (data) => {
        this.userId = data.id;
        this.loadUserWithFavorites(this.userId);
      },
      error: (err) => console.error('Error: ', err)
    });
  }

  // --- PAGINACIÓN ---
  currentPage: number = 1;
  itemsPerPage: number = 10;

  get totalPages(): number {
    return Math.ceil(this.filteredProperties.length / this.itemsPerPage);
  }

  get paginatedProperties(): Property[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.filteredProperties.slice(start, end);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  fetchProperties() {
    this.propertyService.getAllProperties().subscribe({
      next: (data) => {
        if (this.ubicacion) {
          this.properties = data.filter(
            p => p.city.toLowerCase() === this.ubicacion.toLowerCase()
          );
        } else {
          this.properties = data;
        }
        this.extractPropertyTypes();
      },
      error: (err) => console.error('Error fetching properties', err)
    });
  }


  loadUserWithFavorites(userId: number): void {
    this.usuarioService.getUserProfile(userId).subscribe({
      next: (data) => {
        this.usuarioCompleto = data;
        console.log('Usuario completo cargado:', data);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar favoritos:', error);
        this.isLoading = false;
      }
    });
  }

  extractPropertyTypes(): void {
    const types = [...new Set(this.properties.map(p => p.type))];
    this.propertyTypes = types;
  }

  // Filtros
  get filteredProperties(): Property[] {
    let filtered = [...this.properties];

    if (this.selectedType !== 'todos') {
      filtered = filtered.filter(p => p.type === this.selectedType);
    }

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(p =>
        p.title.toLowerCase().includes(term) ||
        p.address.toLowerCase().includes(term) ||
        p.city.toLowerCase().includes(term)
      );
    }

    if (this.minPrice > 0) filtered = filtered.filter(p => p.price >= this.minPrice);
    if (this.maxPrice > 0) filtered = filtered.filter(p => p.price <= this.maxPrice);

    switch (this.selectedSort) {
      case 'precio-asc':
        filtered.sort((a, b) => a.price - b.price);
        break;
      case 'precio-desc':
        filtered.sort((a, b) => b.price - a.price);
        break;
      case 'area-desc':
        filtered.sort((a, b) => b.areaCubierta - a.areaCubierta);
        break;
      case 'reciente':
      default:
        break;
    }

    return filtered;
  }

  filterByUbicacion(): void {
    if (this.ubicacion) {
      this.properties = this.properties.filter(p =>
        p.city.toLowerCase() === this.ubicacion.toLowerCase()
      );
    }
  }

  resetFilters(): void {
    this.selectedType = 'todos';
    this.selectedSort = 'reciente';
    this.minPrice = 0;
    this.maxPrice = 0;
    this.searchTerm = '';
  }

  // Favoritos
  isFavorite(propertyId: number): boolean {
    return this.usuarioCompleto?.favorites?.some(fav => fav.propertyId === propertyId) || false;
  }

  toggleFavorite(propertyId: number): void {
    if (!this.usuarioCompleto) return;

    if (this.isFavorite(propertyId)) {
      this.propertyFavoriteService.removeFavorite(this.userId, propertyId).subscribe({
        next: () => {
          this.usuarioCompleto!.favorites = this.usuarioCompleto!.favorites.filter(fav => fav.propertyId !== propertyId);
        },
        error: (error) => console.error('Error al eliminar de favoritos:', error)
      });
    } else {
      const request: FavoritePropertyRequest = { userId: this.userId, propertyId };
      this.propertyFavoriteService.addFavorite(request).subscribe({
        next: () => this.usuarioCompleto!.favorites.push({ propertyId } as any),
        error: (err) => console.error('Error al agregar a favoritos:', err)
      });
    }
  }

  // Navegación de imágenes
  nextImage(property: any) {
    if (!property.currentImageIndex) property.currentImageIndex = 0;
    property.currentImageIndex = (property.currentImageIndex + 1) % property.images.length;
  }

  prevImage(property: any) {
    if (!property.currentImageIndex) property.currentImageIndex = 0;
    property.currentImageIndex = (property.currentImageIndex - 1 + property.images.length) % property.images.length;
  }
}
