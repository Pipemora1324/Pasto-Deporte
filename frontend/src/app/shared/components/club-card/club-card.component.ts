import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { Club } from '../../../core/models/club.model';
import { EstadoBadgeComponent } from '../estado-badge/estado-badge.component';
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

  esFechaVigente(fecha?: string): boolean {
    if (!fecha) return false;
    return new Date(fecha) >= new Date();
  }

}
