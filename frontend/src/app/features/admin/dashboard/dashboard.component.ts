import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ReporteService } from '../../../core/services/reporte.service';
import { ClubService } from '../../../core/services/club.service';
import { Club } from '../../../core/models/club.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, RouterModule, MatCardModule, MatButtonModule, MatIconModule,
    MatTableModule, MatProgressSpinnerModule, MatDividerModule, MatTooltipModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private reporteService = inject(ReporteService);
  private clubService    = inject(ClubService);

  ultimosClubes: Club[] = [];
  todosClubes: Club[] = [];
  loading = false;
  loadingClubes = false;

  columnasClubes = ['nombre', 'disciplina', 'estado'];

  statCards = [
    { key: 'totalClubes', label: 'Total Clubes', icon: 'groups', color: '#1a5276', bgColor: '#e8f4f8' }
  ];

  ngOnInit(): void { this.cargarClubes(); }

  cargarClubes(): void {
    this.loading = true;
    this.loadingClubes = true;
    this.clubService.getAll().subscribe({
      next: (clubes) => {
        this.todosClubes   = clubes;
        this.ultimosClubes = clubes.slice(0, 8);
        this.loading       = false;
        this.loadingClubes = false;
      },
      error: () => { this.loading = false; this.loadingClubes = false; }
    });
  }

  esFechaVigente(fecha?: string): boolean {
    if (!fecha) return false;
    return new Date(fecha) >= new Date();
  }

  getStatValue(key: string): number {
    const hoy = new Date();
    switch (key) {
      case 'totalClubes': return this.todosClubes.length;
      case 'vigentes':    return this.todosClubes.filter(c => this.esFechaVigente(c.fechaFinReconocimiento)).length;
      case 'noVigentes':  return this.todosClubes.filter(c => !this.esFechaVigente(c.fechaFinReconocimiento)).length;
      default: return 0;
    }
  }

  exportarExcel(): void {
    this.reporteService.exportarExcel().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `clubes-pasto-deporte-${new Date().toISOString().split('T')[0]}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => {}
    });
  }
}
