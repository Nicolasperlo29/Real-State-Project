import { Component, HostListener, OnInit } from '@angular/core';
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
   mobileMenuOpen: boolean = false;

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
  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  closeMobileMenu(): void {
    this.mobileMenuOpen = false;
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    const header = document.querySelector('header');
    if (window.scrollY > 50) {
      header?.classList.add('scrolled');
    } else {
      header?.classList.remove('scrolled');
    }
  }

  @HostListener('window:resize', [])
  onWindowResize(): void {
    if (window.innerWidth > 768 && this.mobileMenuOpen) {
      this.closeMobileMenu();
    }
  }
}
