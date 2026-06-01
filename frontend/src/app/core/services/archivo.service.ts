import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ArchivoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/archivos`;

  subirImagen(file: File): Observable<{ url: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ url: string }>(`${this.apiUrl}/imagen`, formData).pipe(
      catchError(err => throwError(() => err))
    );
  }

  subirPdf(file: File): Observable<{ url: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ url: string }>(`${this.apiUrl}/pdf`, formData).pipe(
      catchError(err => throwError(() => err))
    );
  }
}
