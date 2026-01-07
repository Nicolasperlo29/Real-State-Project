import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../interfaces/user';
import { Observable } from 'rxjs';
import { UserWithFavorites } from '../interfaces/user-with-favorites';

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {

  // private apiUrl = 'http://localhost:8082';
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getUserProfile(userId?: number): Observable<UserWithFavorites> {
    return this.http.get<UserWithFavorites>(`${this.apiUrl}/users/${userId}/profile`);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl + "/users/all");
  }
}
