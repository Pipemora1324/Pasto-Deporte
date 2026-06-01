import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ArchivoService } from '../../../core/services/archivo.service';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
    MatSnackBarModule
  ],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {
  @Input() tipo: 'imagen' | 'pdf' = 'imagen';
  @Input() label = 'Subir archivo';
  @Input() currentUrl?: string;
  @Output() archivoSubido = new EventEmitter<string>();

  private archivoService = inject(ArchivoService);
  private snackBar = inject(MatSnackBar);

  uploading = false;
  previewUrl?: string;

  resolveUrl(url?: string): string {
    if (!url) return '';
    return url.startsWith('/') ? `${environment.apiUrl}${url}` : url;
  }

  get accept(): string {
    return this.tipo === 'imagen' ? 'image/*' : '.pdf,application/pdf';
  }

  get icon(): string {
    return this.tipo === 'imagen' ? 'image' : 'picture_as_pdf';
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || !input.files[0]) return;

    const file = input.files[0];
    this.uploading = true;

    if (this.tipo === 'imagen') {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewUrl = e.target?.result as string;
      };
      reader.readAsDataURL(file);

      this.archivoService.subirImagen(file).subscribe({
        next: (resp) => {
          this.uploading = false;
          this.currentUrl = resp.url;
          this.archivoSubido.emit(resp.url);
          this.snackBar.open('Imagen subida correctamente', 'OK', { duration: 3000 });
        },
        error: (err) => {
          this.uploading = false;
          this.snackBar.open('Error al subir la imagen', 'OK', { duration: 3000 });
        }
      });
    } else {
      this.archivoService.subirPdf(file).subscribe({
        next: (resp) => {
          this.uploading = false;
          this.currentUrl = resp.url;
          this.archivoSubido.emit(resp.url);
          this.snackBar.open('PDF subido correctamente', 'OK', { duration: 3000 });
        },
        error: (err) => {
          this.uploading = false;
          this.snackBar.open('Error al subir el PDF', 'OK', { duration: 3000 });
        }
      });
    }
  }
}
