import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OpcionMenuResponse } from '../models/menu.model';

/**
 * Servicio para obtener el árbol de menú de navegación desde el backend.
 * Si la petición falla, retorna el menú por defecto para no romper la navegación.
 */
@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private http    = inject(HttpClient);
  private apiUrl  = `${environment.apiUrl}/menu`;

  /** Menú de respaldo si el backend no responde */
  private readonly fallback: OpcionMenuResponse[] = [
    { id: 1, nombre: 'Dashboard',         ruta: '/admin/dashboard', icono: 'dashboard', orden: 1, hijos: [] },
    { id: 2, nombre: 'Gestión de Clubes', ruta: '/admin/clubes',    icono: 'groups',    orden: 2, hijos: [] }
  ];

  /**
   * Obtiene el árbol completo del menú desde el backend.
   * En caso de error retorna el menú por defecto hardcodeado.
   *
   * @returns Observable con la lista de ítems raíz del menú
   */
  getMenu(): Observable<OpcionMenuResponse[]> {
    return this.http.get<OpcionMenuResponse[]>(this.apiUrl).pipe(
      catchError(() => of(this.fallback))
    );
  }
}
