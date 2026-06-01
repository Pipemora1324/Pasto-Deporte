import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EstadoClub } from '../../../core/models/club.model';

@Component({
  selector: 'app-estado-badge',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './estado-badge.component.html',
  styleUrls: ['./estado-badge.component.scss']
})
export class EstadoBadgeComponent {
  @Input() estado!: EstadoClub;

  get label(): string {
    return this.estado === EstadoClub.VIGENTE ? 'Vigente' : 'No Vigente';
  }

  get colorClass(): string {
    return this.estado === EstadoClub.VIGENTE ? 'badge-vigente' : 'badge-no-vigente';
  }
}
