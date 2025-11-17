import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { User } from '../../interfaces/user';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  form: FormGroup;
  showModalAlta = false;
  showModalUsuarios = false;
  roles = ['USER', 'AGENT'];
  usuarios: User[] = [];

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.form = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      acceptTerms: [true],
      role: ['AGENT']
    });
  }

  openModalAlta() {
    this.showModalAlta = true;
  }

  openModalUsuarios() {
    this.showModalUsuarios = true;
    this.onViewUsers();
  }

  closeAllModals() {
    this.showModalAlta = false;
    this.showModalUsuarios = false;
  }

  showStats() {
    alert('📊 Aquí podrías mostrar estadísticas de usuarios o agentes');
  }

  onSubmit() {
    if (this.form.invalid) return;
    const request = this.form.value;
    this.userService.registerUser(request).subscribe({
      next: () => {
        alert('Agente creado correctamente ✅');
        this.closeAllModals();
        this.form.reset({ role: 'AGENT', acceptTerms: true });
      },
      error: () => alert('Error al registrar el agente ❌')
    });
  }

  onViewUsers() {
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.usuarios = data
        console.log('usuarios: ', this.usuarios);
      },
      error: () => alert('❌ Error al obtener la lista de usuarios')
    });
  }
}
