import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MiembroOrgano } from '../models/club.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MiembroOrganoService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  getByClub(clubId: number): Observable<MiembroOrgano[]> {
    return this.http.get<MiembroOrgano[]>(`${this.base}/clubes/${clubId}/miembros`);
  }

  crear(clubId: number, data: Partial<MiembroOrgano>): Observable<MiembroOrgano> {
    return this.http.post<MiembroOrgano>(`${this.base}/clubes/${clubId}/miembros`, data);
  }

  actualizar(id: number, data: Partial<MiembroOrgano>): Observable<MiembroOrgano> {
    return this.http.put<MiembroOrgano>(`${this.base}/miembros/${id}`, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/miembros/${id}`);
  }
}
