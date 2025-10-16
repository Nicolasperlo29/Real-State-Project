import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  user?: User;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getProfile().subscribe(user => {
      this.user = user;
      console.log('Usuario cargado en perfil:', this.user);
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    window.location.href = '/login';
  }
}
