import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Club } from '../models/club.model';

/**
 * Servicio Angular para la gestión de clubes deportivos.
 *
 * Se comunica con el backend REST a través de HTTP y actúa como
 * capa de abstracción entre los componentes y la API.
 *
 * Pilares POO aplicados:
 * - ABSTRACCIÓN: oculta los detalles de comunicación HTTP a los componentes.
 * - ENCAPSULAMIENTO: la URL base y el cliente HTTP son privados.
 * - MODULARIDAD: servicio dedicado exclusivamente a operaciones de clubes.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 */
@Injectable({
  providedIn: 'root'
})
export class ClubService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/clubes`;

  /** Obtiene todos los clubes activos del sistema. */
  getAll(): Observable<Club[]> {
    return this.http.get<Club[]>(this.apiUrl).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /**
   * Busca clubes aplicando filtros opcionales de nombre, disciplina y estado.
   * @param nombre     Fragmento del nombre a filtrar (opcional).
   * @param disciplina Fragmento de la disciplina (opcional).
   * @param estado     Estado de vigencia: 'VIGENTE' o 'NO_VIGENTE' (opcional).
   */
  buscar(nombre?: string, disciplina?: string, estado?: string): Observable<Club[]> {
    let params = new HttpParams();
    if (nombre)     params = params.set('nombre', nombre);
    if (disciplina) params = params.set('disciplina', disciplina);
    if (estado)     params = params.set('estado', estado);
    return this.http.get<Club[]>(`${this.apiUrl}/buscar`, { params }).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /**
   * Obtiene un club por su identificador único.
   * @param id Identificador del club.
   */
  getById(id: number): Observable<Club> {
    return this.http.get<Club>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /**
   * Crea un nuevo club en el sistema.
   * @param club Datos del club a crear.
   */
  crear(club: Club): Observable<Club> {
    return this.http.post<Club>(this.apiUrl, club).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /**
   * Actualiza los datos de un club existente.
   * @param id  Identificador del club a actualizar.
   * @param club Nuevos datos del club.
   */
  actualizar(id: number, club: Club): Observable<Club> {
    return this.http.put<Club>(`${this.apiUrl}/${id}`, club).pipe(
      catchError(err => throwError(() => err))
    );
  }

  /**
   * Elimina lógicamente (soft delete) un club por su ID.
   * @param id Identificador del club a eliminar.
   */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => err))
    );
  }
}
