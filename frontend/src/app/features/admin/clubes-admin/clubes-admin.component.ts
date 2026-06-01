import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { ClubService } from '../../../core/services/club.service';
import { Club } from '../../../core/models/club.model';

@Component({
  selector: 'app-clubes-admin',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatTooltipModule,
    MatPaginatorModule,
    ConfirmDialogComponent
  ],
  templateUrl: './clubes-admin.component.html',
  styleUrls: ['./clubes-admin.component.scss']
})
export class ClubesAdminComponent implements OnInit {
  @ViewChild(MatPaginator) set paginator(p: MatPaginator) {
    if (p) this.dataSource.paginator = p;
  }

  private clubService = inject(ClubService);
  private snackBar    = inject(MatSnackBar);
  private dialog      = inject(MatDialog);

  dataSource     = new MatTableDataSource<Club>([]);
  loading        = false;
  displayedColumns = ['nombre', 'disciplina', 'estado', 'acciones'];

  private _searchText = '';
  get searchText()         { return this._searchText; }
  set searchText(val: string) {
    this._searchText = val;
    this.dataSource.filter = val.trim().toLowerCase();
  }

  ngOnInit(): void {
    this.dataSource.filterPredicate = (club, filter) =>
      club.nombre.toLowerCase().includes(filter)
      || club.disciplinaDeportiva.toLowerCase().includes(filter)
      || (club.direccion?.toLowerCase().includes(filter) ?? false)
      || club.estado.toLowerCase().includes(filter);

    this.cargarClubes();
  }

  cargarClubes(): void {
    this.loading = true;
    this.clubService.getAll().subscribe({
      next: (clubes) => {
        this.dataSource.data = clubes;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar clubes', 'OK', { duration: 3000 });
      }
    });
  }

  esFechaVigente(fecha?: string): boolean {
    if (!fecha) return false;
    return new Date(fecha) >= new Date();
  }

  eliminar(club: Club): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        titulo: 'Eliminar Club',
        mensaje: `¿Estás seguro de que deseas eliminar el club "${club.nombre}"? Esta acción no se puede deshacer.`
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed && club.id) {
        this.clubService.eliminar(club.id).subscribe({
          next: () => {
            this.dataSource.data = this.dataSource.data.filter(c => c.id !== club.id);
            this.snackBar.open('Club eliminado correctamente', 'OK', { duration: 3000 });
          },
          error: () => {
            this.snackBar.open('Error al eliminar el club', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }
}
