import { Component, OnInit, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { Component as NgComponent } from '@angular/core';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MiembroOrganoService } from '../../../core/services/miembro-organo.service';
import { ClubService } from '../../../core/services/club.service';
import { MiembroOrgano, TipoOrgano, Club } from '../../../core/models/club.model';

@NgComponent({
  selector: 'app-miembro-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <h2 mat-dialog-title>{{ data.miembro ? 'Editar Miembro' : 'Nuevo Miembro' }}</h2>
    <mat-dialog-content>
      <form [formGroup]="form" class="dialog-form">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Nombre completo *</mat-label>
          <input matInput formControlName="nombre">
          <mat-error *ngIf="form.get('nombre')?.hasError('required')">Requerido</mat-error>
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Tipo de Órgano *</mat-label>
          <mat-select formControlName="tipoOrgano">
            <mat-option *ngFor="let t of tipos" [value]="t.value">{{ t.label }}</mat-option>
          </mat-select>
          <mat-error *ngIf="form.get('tipoOrgano')?.hasError('required')">Requerido</mat-error>
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Cargo</mat-label>
          <input matInput formControlName="cargo" placeholder="Ej: Presidente, Secretaria, Tesorera">
          <mat-icon matSuffix>work</mat-icon>
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Cédula</mat-label>
          <input matInput formControlName="cedula">
        </mat-form-field>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-raised-button color="primary" (click)="guardar()" [disabled]="form.invalid">Guardar</button>
    </mat-dialog-actions>
  `,
  styles: [`
    .dialog-form { display: flex; flex-direction: column; gap: 8px; min-width: 420px; padding-top: 8px; }
    .full-width { width: 100%; }
    @media (max-width: 500px) { .dialog-form { min-width: 280px; } }
  `]
})
export class MiembroDialogComponent {
  private fb = inject(FormBuilder);

  tipos = [
    { value: TipoOrgano.ORGANO_ADMINISTRACION, label: 'Órgano de Administración' },
    { value: TipoOrgano.COMISION_DISCIPLINARIA, label: 'Comisión Disciplinaria'   },
    { value: TipoOrgano.ORGANO_CONTROL,         label: 'Órgano de Control'        }
  ];

  form = this.fb.group({
    nombre:     [this.data.miembro?.nombre     || '', Validators.required],
    tipoOrgano: [this.data.miembro?.tipoOrgano || TipoOrgano.ORGANO_ADMINISTRACION, Validators.required],
    cargo:      [this.data.miembro?.cargo      || ''],
    cedula:     [this.data.miembro?.cedula     || '']
  });

  constructor(
    public dialogRef: MatDialogRef<MiembroDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { miembro?: MiembroOrgano }
  ) {}

  guardar(): void {
    if (this.form.valid) { this.dialogRef.close(this.form.value); }
  }
}

@Component({
  selector: 'app-directorio',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDialogModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatDividerModule,
    ConfirmDialogComponent,
    MiembroDialogComponent
  ],
  templateUrl: './directorio.component.html',
  styleUrls: ['./directorio.component.scss']
})
export class DirectorioComponent implements OnInit {
  private route          = inject(ActivatedRoute);
  private miembroService = inject(MiembroOrganoService);
  private clubService    = inject(ClubService);
  private dialog         = inject(MatDialog);
  private snackBar       = inject(MatSnackBar);

  clubId!: number;
  club?: Club;
  miembros: MiembroOrgano[] = [];
  loading = false;

  displayedColumns = ['nombre', 'cargo', 'cedula', 'acciones'];

  secciones = [
    { tipo: TipoOrgano.ORGANO_ADMINISTRACION, label: 'Órgano de Administración', icon: 'admin_panel_settings' },
    { tipo: TipoOrgano.COMISION_DISCIPLINARIA, label: 'Comisión Disciplinaria',  icon: 'gavel'               },
    { tipo: TipoOrgano.ORGANO_CONTROL,         label: 'Órgano de Control',       icon: 'shield'              }
  ];

  ngOnInit(): void {
    this.clubId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarClub();
    this.cargarMiembros();
  }

  cargarClub(): void {
    this.clubService.getById(this.clubId).subscribe({
      next: (club) => { this.club = club; },
      error: () => {}
    });
  }

  cargarMiembros(): void {
    this.loading = true;
    this.miembroService.getByClub(this.clubId).subscribe({
      next: (m) => { this.miembros = m; this.loading = false; },
      error: ()  => { this.loading = false; }
    });
  }

  miembrosPorTipo(tipo: TipoOrgano): MiembroOrgano[] {
    return this.miembros.filter(m => m.tipoOrgano === tipo);
  }

  abrirDialog(miembro?: MiembroOrgano): void {
    const dialogRef = this.dialog.open(MiembroDialogComponent, {
      width: '480px',
      data: { miembro }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) { return; }
      if (miembro?.id) {
        this.miembroService.actualizar(miembro.id, result).subscribe({
          next: () => { this.cargarMiembros(); this.snackBar.open('Miembro actualizado', 'OK', { duration: 3000 }); },
          error: () =>   this.snackBar.open('Error al actualizar', 'OK', { duration: 3000 })
        });
      } else {
        this.miembroService.crear(this.clubId, result).subscribe({
          next: () => { this.cargarMiembros(); this.snackBar.open('Miembro creado', 'OK', { duration: 3000 }); },
          error: () =>   this.snackBar.open('Error al crear', 'OK', { duration: 3000 })
        });
      }
    });
  }

  eliminar(miembro: MiembroOrgano): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        titulo:  'Eliminar Miembro',
        mensaje: `¿Eliminar a ${miembro.nombre} del órgano?`
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed && miembro.id) {
        this.miembroService.eliminar(miembro.id).subscribe({
          next: () => {
            this.miembros = this.miembros.filter(m => m.id !== miembro.id);
            this.snackBar.open('Miembro eliminado', 'OK', { duration: 3000 });
          },
          error: () => this.snackBar.open('Error al eliminar', 'OK', { duration: 3000 })
        });
      }
    });
  }
}
