import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Property } from '../interfaces/property';

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  // private apiUrl = 'http://localhost:8083/properties';
  private apiUrl = 'http://localhost:8080/properties';

  constructor(private http: HttpClient) { }

  getAllProperties(): Observable<Property[]> {
    return this.http.get<Property[]>(this.apiUrl + "/list");
  }

  getPropertyById(id: number): Observable<Property> {
    return this.http.get<Property>(`${this.apiUrl}/${id}`);
  }

  searchByCity(city: string): Observable<Property[]> {
    return this.http.get<Property[]>(`${this.apiUrl}/search?city=${city}`);
  }

  createProperty(property: Property): Observable<Property> {
    return this.http.post<Property>(this.apiUrl + '/create', property);
  }

  sendContactEmail(contactData: any) {
    return this.http.post('http://localhost:8084/api/contact', contactData);
  }
}
