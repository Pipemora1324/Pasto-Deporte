import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { ClubCardComponent } from '../../../shared/components/club-card/club-card.component';
import { ClubService } from '../../../core/services/club.service';
import { Club } from '../../../core/models/club.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    NavbarComponent,
    ClubCardComponent
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  private clubService = inject(ClubService);

  clubes: Club[] = [];
  loading = false;
  searchNombre = '';

  ngOnInit(): void { this.cargarClubes(); }

  cargarClubes(): void {
    this.loading = true;
    this.clubService.getAll().subscribe({
      next: (clubes) => { this.clubes = clubes; this.loading = false; },
      error: ()       => { this.loading = false; }
    });
  }

  get clubesFiltrados(): Club[] {
    return this.clubes.filter(c =>
      !this.searchNombre || c.nombre.toLowerCase().includes(this.searchNombre.toLowerCase())
    );
  }

  limpiarFiltros(): void { this.searchNombre = ''; }
}
