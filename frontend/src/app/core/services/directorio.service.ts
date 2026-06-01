import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MiembroOrgano } from '../models/club.model';

/** @deprecated Use MiembroOrganoService instead */
@Injectable({ providedIn: 'root' })
export class DirectorioService {
  private http    = inject(HttpClient);
  private apiUrl  = environment.apiUrl;

  getByClub(clubId: number): Observable<MiembroOrgano[]> {
    return this.http.get<MiembroOrgano[]>(`${this.apiUrl}/clubes/${clubId}/miembros`);
  }

  crear(clubId: number, data: Partial<MiembroOrgano>): Observable<MiembroOrgano> {
    return this.http.post<MiembroOrgano>(`${this.apiUrl}/clubes/${clubId}/miembros`, data);
  }

  actualizar(id: number, data: Partial<MiembroOrgano>): Observable<MiembroOrgano> {
    return this.http.put<MiembroOrgano>(`${this.apiUrl}/miembros/${id}`, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/miembros/${id}`);
  }
}
