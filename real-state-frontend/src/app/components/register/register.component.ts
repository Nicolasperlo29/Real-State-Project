import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {

  form!: FormGroup;
  submitted = false;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.form = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    },
      { validators: this.passwordMatchValidator })
  }

  ngOnInit(): void {
  }

  // Validador personalizado para confirmar contraseña
  passwordMatchValidator(control: AbstractControl) {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    if (password !== confirmPassword) {
      control.get('confirmPassword')?.setErrors({ mismatch: true });
    } else {
      control.get('confirmPassword')?.setErrors(null);
    }
    return null;
  }

  // Getter para fácil acceso en el template
  get f() { return this.form.controls; }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid) {
      return;
    }

    const { email, password } = this.form.value;

    this.userService.registerUser({ email, password }).subscribe({
      next: (response) => {
        console.log('Usuario registrado', response);
        this.userService.login({ email, password }).subscribe({
          next: (loginResponse) => {
            console.log('Usuario logueado', loginResponse);
            localStorage.setItem('user', JSON.stringify(loginResponse));
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('email', email);
            window.location.href = '/properties';
          },
          error: (loginError) => {
            console.error('Error al loguear usuario', loginError);
          }
        });
      },
      error: (error) => {
        console.error('Error al registrar usuario', error);
      }
    });
  }
}
