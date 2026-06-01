import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { EstadoBadgeComponent } from '../../../shared/components/estado-badge/estado-badge.component';
import { ClubService } from '../../../core/services/club.service';
import { MiembroOrganoService } from '../../../core/services/miembro-organo.service';
import { Club, MiembroOrgano, TipoOrgano } from '../../../core/models/club.model';
import { environment } from '../../../../environments/environment';
import { SafeUrlPipe } from '../../../shared/pipes/safe-url.pipe';

@Component({
  selector: 'app-club-detalle-publico',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatChipsModule,
    NavbarComponent,
    EstadoBadgeComponent,
    SafeUrlPipe
  ],
  templateUrl: './club-detalle-publico.component.html',
  styleUrls: ['./club-detalle-publico.component.scss']
})
export class ClubDetallePublicoComponent implements OnInit {
  private route          = inject(ActivatedRoute);
  private clubService    = inject(ClubService);
  private miembroService = inject(MiembroOrganoService);

  club?: Club;
  miembros: MiembroOrgano[] = [];
  loading = true;

  columnsMiembros = ['nombre', 'cargo', 'cedula'];

  secciones = [
    { tipo: TipoOrgano.ORGANO_ADMINISTRACION, label: 'Órgano de Administración', icon: 'admin_panel_settings' },
    { tipo: TipoOrgano.COMISION_DISCIPLINARIA, label: 'Comisión Disciplinaria',  icon: 'gavel'               },
    { tipo: TipoOrgano.ORGANO_CONTROL,         label: 'Órgano de Control',       icon: 'shield'              }
  ];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarClub(id);
  }

  cargarClub(id: number): void {
    this.loading = true;
    this.clubService.getById(id).subscribe({
      next: (club) => {
        this.club = club;
        this.loading = false;
        this.cargarMiembros(id);
      },
      error: () => { this.loading = false; }
    });
  }

  cargarMiembros(clubId: number): void {
    this.miembroService.getByClub(clubId).subscribe({
      next: (m) => { this.miembros = m; },
      error: ()  => {}
    });
  }

  miembrosPorTipo(tipo: TipoOrgano): MiembroOrgano[] {
    return this.miembros.filter(m => m.tipoOrgano === tipo);
  }

  resolveUrl(url?: string): string {
    if (!url) return '';
    return url.startsWith('/') ? `${environment.apiUrl}${url}` : url;
  }

  esFechaVigente(fecha?: string): boolean {
    if (!fecha) return false;
    return new Date(fecha) >= new Date();
  }

  descargarPdf(): void {
    if (this.club?.documentoPdfUrl) {
      window.open(this.resolveUrl(this.club.documentoPdfUrl), '_blank');
    }
  }
}
