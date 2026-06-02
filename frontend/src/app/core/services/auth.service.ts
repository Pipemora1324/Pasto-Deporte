import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse } from '../models/auth.model';

/**
 * Servicio Angular para autenticación y gestión de sesión JWT.
 *
 * Gestiona el ciclo completo de autenticación: login, logout, refresco
 * de token y persistencia de credenciales en localStorage.
 *
 * Pilares POO aplicados:
 * - ENCAPSULAMIENTO: las claves de localStorage y la URL son privadas.
 * - OCULTAMIENTO: el token JWT nunca se expone directamente; solo se
 *   accede mediante métodos controlados (getToken, getRefreshToken).
 * - MODULARIDAD: clase dedicada exclusivamente a autenticación.
 * - ABSTRACCIÓN: los componentes no conocen el mecanismo JWT subyacente.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http   = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = environment.apiUrl;

  private readonly TOKEN_KEY   = 'accessToken';
  private readonly REFRESH_KEY = 'refreshToken';
  private readonly USER_KEY    = 'userInfo';

  /**
   * Autentica al usuario y persiste los tokens JWT en localStorage.
   * @param credentials Credenciales de inicio de sesión.
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.accessToken);
        localStorage.setItem(this.REFRESH_KEY, response.refreshToken);
        localStorage.setItem(this.USER_KEY, JSON.stringify({
          username: response.username,
          nombre:   response.nombre,
          rol:      response.rol
        }));
      }),
      catchError(err => throwError(() => err))
    );
  }

  /** Cierra la sesión eliminando todos los tokens y redirige al login. */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/admin/login']);
  }

  /** Retorna el token de acceso actual o null si no hay sesión. */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /** Retorna el token de refresco actual o null si no hay sesión. */
  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_KEY);
  }

  /** Indica si existe una sesión activa (token presente). */
  isLoggedIn(): boolean {
    const token = this.getToken();
    return token !== null && token !== '';
  }

  /** Retorna la información del usuario autenticado desde localStorage. */
  getUserInfo(): { username: string; nombre: string; rol: string } | null {
    const userStr = localStorage.getItem(this.USER_KEY);
    if (userStr) {
      try { return JSON.parse(userStr); } catch { return null; }
    }
    return null;
  }

  /**
   * Solicita un nuevo access token usando el refresh token almacenado.
   * Si falla, cierra la sesión automáticamente.
   */
  refreshToken(): Observable<AuthResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/refresh`, { refreshToken }).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.accessToken);
        if (response.refreshToken) {
          localStorage.setItem(this.REFRESH_KEY, response.refreshToken);
        }
      }),
      catchError(err => {
        this.logout();
        return throwError(() => err);
      })
    );
  }
}
