import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/reportes`;

  exportarExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/clubes-excel`, {
      responseType: 'blob'
    }).pipe(
      catchError(err => throwError(() => err))
    );
  }
}
