import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { Club } from '../../../core/models/club.model';
import { EstadoBadgeComponent } from '../estado-badge/estado-badge.component';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-club-card',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    EstadoBadgeComponent
  ],
  templateUrl: './club-card.component.html',
  styleUrls: ['./club-card.component.scss']
})
export class ClubCardComponent {
  @Input() club!: Club;

  resolveUrl(url?: string): string {
    if (!url) return '';
    if (!url.startsWith('/')) return url;
    const path = url.startsWith('/api/') ? url.substring(4) : url;
    return `${environment.apiUrl}${path}`;
  }

  esFechaVigente(fecha?: string): boolean {
    if (!fecha) return false;
    return new Date(fecha) >= new Date();
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = 'assets/images/club-default.png';
    img.onerror = null;
  }
}
