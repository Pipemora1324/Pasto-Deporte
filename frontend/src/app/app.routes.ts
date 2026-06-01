import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/public/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'clubes',
    redirectTo: '',
    pathMatch: 'full'
  },
  {
    path: 'clubes/:id',
    loadComponent: () =>
      import('./features/public/club-detalle-publico/club-detalle-publico.component').then(m => m.ClubDetallePublicoComponent)
  },
  {
    path: 'admin/login',
    loadComponent: () =>
      import('./features/admin/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin',
    loadComponent: () =>
      import('./features/admin/admin-layout/admin-layout.component').then(m => m.AdminLayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/admin/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'clubes',
        loadComponent: () =>
          import('./features/admin/clubes-admin/clubes-admin.component').then(m => m.ClubesAdminComponent)
      },
      {
        path: 'clubes/nuevo',
        loadComponent: () =>
          import('./features/admin/club-form/club-form.component').then(m => m.ClubFormComponent)
      },
      {
        path: 'clubes/:id/editar',
        loadComponent: () =>
          import('./features/admin/club-form/club-form.component').then(m => m.ClubFormComponent)
      },
      {
        path: 'clubes/:id/directorio',
        loadComponent: () =>
          import('./features/admin/directorio/directorio.component').then(m => m.DirectorioComponent)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '' }
];
