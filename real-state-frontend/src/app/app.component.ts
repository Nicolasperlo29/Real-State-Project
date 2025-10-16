import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'real-state-frontend';

  user: any = null;
  loggedIn: boolean = false;

  constructor(private authService: UserService) { }
  
  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.loggedIn = true;
      this.authService.getProfile().subscribe(user => {
        this.user = user;
        console.log('Usuario cargado:', this.user);
      });
    }
  }
}
