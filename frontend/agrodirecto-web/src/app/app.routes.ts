import { Routes } from '@angular/router';
import { AccessComponent } from './access.component';
import { StorefrontComponent } from './storefront.component';
import { AdminComponent } from './admin.component';

export const routes: Routes = [
  { path: '', component: AccessComponent, title: 'AgroDirecto | Acceso' },
  { path: 'tienda', component: StorefrontComponent, title: 'AgroDirecto | Tienda' },
  { path: 'admin', component: AdminComponent, title: 'AgroDirecto | Administración' },
  { path: '**', redirectTo: '' }
];
