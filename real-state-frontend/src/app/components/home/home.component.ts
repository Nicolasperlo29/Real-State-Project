import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Property } from '../../interfaces/property';
import { PropertyService } from '../../services/property.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  properties: Property[] = [];

  constructor(private propertyService: PropertyService) { }

  ngOnInit(): void {
    this.propertyService.getAllProperties().subscribe(data => {
      this.properties = data;
    });
  }
}
