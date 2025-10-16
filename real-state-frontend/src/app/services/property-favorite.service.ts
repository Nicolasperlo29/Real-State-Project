import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FavoritePropertyRequest } from '../interfaces/favorite-property-request';
import { Observable } from 'rxjs';
import { FavoriteProperty } from '../interfaces/favorite-property';

@Injectable({
  providedIn: 'root'
})
export class PropertyFavoriteService {

  private baseUrl = 'http://localhost:8083/favorites'; // Cambiar al URL de tu microservicio

  constructor(private http: HttpClient) { }

  addFavorite(request: FavoritePropertyRequest): Observable<FavoriteProperty> {
    return this.http.post<FavoriteProperty>(`${this.baseUrl}`, request);
  }

  removeFavorite(userId: number, propertyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/user/${userId}/property/${propertyId}`);
  }

  getFavoritesByUser(userId: number): Observable<FavoriteProperty[]> {
    return this.http.get<FavoriteProperty[]>(`${this.baseUrl}/user/${userId}`);
  }
}
