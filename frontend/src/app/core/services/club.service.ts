import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Club } from '../models/club.model';

@Injectable({
  providedIn: 'root'
})
export class ClubService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/clubes`;

  getAll(): Observable<Club[]> {
    return this.http.get<Club[]>(this.apiUrl).pipe(
      catchError(err => throwError(() => err))
    );
  }

  buscar(nombre?: string, disciplina?: string, estado?: string): Observable<Club[]> {
    let params = new HttpParams();
    if (nombre) params = params.set('nombre', nombre);
    if (disciplina) params = params.set('disciplina', disciplina);
    if (estado) params = params.set('estado', estado);
    return this.http.get<Club[]>(`${this.apiUrl}/buscar`, { params }).pipe(
      catchError(err => throwError(() => err))
    );
  }

  getById(id: number): Observable<Club> {
    return this.http.get<Club>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => err))
    );
  }

  crear(club: Club): Observable<Club> {
    return this.http.post<Club>(this.apiUrl, club).pipe(
      catchError(err => throwError(() => err))
    );
  }

  actualizar(id: number, club: Club): Observable<Club> {
    return this.http.put<Club>(`${this.apiUrl}/${id}`, club).pipe(
      catchError(err => throwError(() => err))
    );
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => err))
    );
  }

}
