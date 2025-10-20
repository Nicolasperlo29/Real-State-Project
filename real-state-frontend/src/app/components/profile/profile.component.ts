import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user';
import { UsuariosService } from '../../services/usuarios.service';
import { UserWithFavorites } from '../../interfaces/user-with-favorites';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  user?: User;
  usuarioCompleto?: UserWithFavorites;
  isLoading = true;

  constructor(
    private userService: UserService,
    private usuarioService: UsuariosService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.userService.getProfile().subscribe({
      next: (user) => {
        this.user = user;
        console.log('Usuario cargado en perfil:', this.user);
        this.loadUserWithFavorites(user.id);
      },
      error: (error) => {
        console.error('Error al cargar usuario:', error);
        this.isLoading = false;
      }
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

  getInitials(fullName: string): string {
    if (!fullName) return 'U';
    return fullName
      .split(' ')
      .map(name => name.charAt(0).toUpperCase())
      .join('')
      .substring(0, 2);
  }

  changeProfileImage(): void {
    // TODO: Implementar cambio de imagen de perfil
    console.log('Cambiar imagen de perfil');
  }

  editProfile(): void {
    this.router.navigate(['/profile/edit']);
  }

  goToSettings(): void {
    this.router.navigate(['/settings']);
  }

  goToProperties(): void {
    this.router.navigate(['/properties']);
  }

  toggleFavorite(propertyId: number): void {
    // TODO: Implementar eliminar de favoritos
    console.log('Toggle favorite:', propertyId);
    // Llamar al servicio para eliminar de favoritos
    // Luego recargar la lista
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    this.router.navigate(['/login']);
  }

  getFavoriteCount(): number {
    return this.usuarioCompleto?.favorites?.length || 0;
  }

  hasFavorites(): boolean {
    return this.getFavoriteCount() > 0;
  }
}
