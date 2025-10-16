import { Routes } from '@angular/router';
import { PropertyListComponent } from './components/property-list/property-list.component';
import { PropertyDetailsComponent } from './components/property-details/property-details.component';
import { CompareComponent } from './components/compare/compare.component';
import { HomeComponent } from './components/home/home.component';
import { CreatePropertyComponent } from './components/create-property/create-property.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'list', component: PropertyListComponent },
    { path: 'property/:id', component: PropertyDetailsComponent },
    { path: 'compare', component: CompareComponent },
    { path: 'login', component: LoginComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'create', component: CreatePropertyComponent },
    { path: 'register', component: RegisterComponent },
    { path: '**', redirectTo: '' } // fallback
];
