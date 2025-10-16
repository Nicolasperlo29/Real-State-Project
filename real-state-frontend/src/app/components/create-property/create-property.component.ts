import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PropertyService } from '../../services/property.service';

@Component({
  selector: 'app-create-property',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-property.component.html',
  styleUrl: './create-property.component.css'
})
export class CreatePropertyComponent implements OnInit {

  propertyForm!: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient, private service: PropertyService) { }

  ngOnInit(): void {
    this.propertyForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      city: ['', Validators.required],
      address: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      areaCubierta: [0, [Validators.required, Validators.min(0)]],
      areaTotal: [0, [Validators.required, Validators.min(0)]],
      rooms: [1, [Validators.required, Validators.min(1)]],
      bathrooms: [1, [Validators.required, Validators.min(1)]],
      type: ['', Validators.required],
      status: ['', Validators.required],
      latitude: [0, Validators.required],
      longitude: [0, Validators.required],
      images: this.fb.array([this.fb.control('')]) // por lo menos una imagen
    });
  }

  get images() {
    return this.propertyForm.get('images') as FormArray;
  }

  addImage() {
    this.images.push(this.fb.control(''));
  }

  removeImage(index: number) {
    this.images.removeAt(index);
  }

  onSubmit() {
    if (this.propertyForm.valid) {
      const property = this.propertyForm.value;
      console.log('Creating property:', property);
      this.service.createProperty(property).subscribe({
        next: (response) => {
          alert('Propiedad creada con éxito');
          this.propertyForm.reset();
          while (this.images.length > 1) {
            this.images.removeAt(0);
          }
        },
        error: (err) => {
          console.error('Error creating property', err);
          alert('Error al crear la propiedad');
        }
      });
    }
  }
}
