import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userInfo');
        router.navigate(['/admin/login']);
      } else if (error.status === 403 && router.url.startsWith('/admin')) {
        router.navigate(['/admin/login']);
      } else if (error.status === 0) {
        console.error('Error de red: No se pudo conectar con el servidor');
      } else {
        console.error(`Error ${error.status}: ${error.message}`);
      }
      return throwError(() => error);
    })
  );
};
