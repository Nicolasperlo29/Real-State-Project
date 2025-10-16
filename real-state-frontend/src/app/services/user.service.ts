import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserRequest } from '../interfaces/user-request';
import { LoginRequest } from '../interfaces/login-request';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8081/auth';

  constructor(private http: HttpClient) { }

  registerUser(request: UserRequest) {
    return this.http.post(`${this.apiUrl}/register`, request);
  }

  login(request: LoginRequest) {
    return this.http.post(`${this.apiUrl}/login`, request);
  }

  getProfile(): Observable<User> {
    const token = localStorage.getItem('token');
    return this.http.get<User>(`${this.apiUrl}/me`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
