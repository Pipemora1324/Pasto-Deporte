import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import {
  ReactiveFormsModule, FormBuilder, FormArray, FormGroup,
  Validators, AbstractControl, ValidationErrors, ValidatorFn
} from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ClubService } from '../../../core/services/club.service';
import { MiembroOrganoService } from '../../../core/services/miembro-organo.service';
import { Club, TipoOrgano } from '../../../core/models/club.model';

function rangoFechas(inicioKey: string, finKey: string, errKey: string): ValidatorFn {
  return (g: AbstractControl): ValidationErrors | null => {
    const ini = g.get(inicioKey)?.value;
    const fin = g.get(finKey)?.value;
    return ini && fin && fin < ini ? { [errKey]: true } : null;
  };
}

@Component({
  selector: 'app-club-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatTooltipModule
  ],
  templateUrl: './club-form.component.html',
  styleUrls: ['./club-form.component.scss']
})
export class ClubFormComponent implements OnInit {
  private fb             = inject(FormBuilder);
  private clubService    = inject(ClubService);
  private miembroService = inject(MiembroOrganoService);
  private router         = inject(Router);
  private route          = inject(ActivatedRoute);
  private snackBar       = inject(MatSnackBar);

  clubId?: number;
  loading   = false;
  saving    = false;
  isEditing = false;

  private soloLetras    = Validators.pattern('^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$');
  private soloNumeros   = Validators.pattern('^[0-9]+$');
  private soloNumPuntos = Validators.pattern('^[0-9.]+$');

  form = this.fb.group({
    nombre:               ['', [Validators.required, Validators.minLength(3), this.soloLetras]],
    disciplinaDeportiva:  ['', [Validators.required, Validators.pattern('^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s,]+$')]],
    descripcionDeportiva: [''],
    direccion:            [''],
    telefono:             ['', [Validators.pattern('^[0-9]{7,10}$')]],
    email:                ['', Validators.email],
    numeroResolucion:          ['', [Validators.required, this.soloNumeros]],
    fechaExpedicionResolucion: [''],
    fechaInicioReconocimiento: [''],
    fechaFinReconocimiento:    [''],
    fechaInicioOrganoAdmon: [''],
    fechaFinOrganoAdmon:    [''],
    representanteLegalNombre: ['', [Validators.required, this.soloLetras]],
    representanteLegalCedula: ['', [Validators.required, this.soloNumPuntos]],
    representanteLegalCargo:  [''],
    organoAdministracion:  this.fb.array([]),
    comisionDisciplinaria: this.fb.array([]),
    organoControl:         this.fb.array([])
  }, {
    validators: [
      rangoFechas('fechaInicioReconocimiento', 'fechaFinReconocimiento', 'reconocimientoInvalido'),
      rangoFechas('fechaInicioOrganoAdmon',    'fechaFinOrganoAdmon',    'organoInvalido')
    ]
  });

  get organoAdmonArray()   { return this.form.get('organoAdministracion')  as FormArray; }
  get comisionArray()      { return this.form.get('comisionDisciplinaria') as FormArray; }
  get organoControlArray() { return this.form.get('organoControl')         as FormArray; }

  asGroup(ctrl: any): FormGroup { return ctrl as FormGroup; }

  private mkMiembroConCargo(): FormGroup {
    return this.fb.group({
      nombre: ['', this.soloLetras],
      cedula: ['', this.soloNumPuntos],
      cargo:  ['', this.soloLetras]
    });
  }
  private mkMiembroSimple(): FormGroup {
    return this.fb.group({
      nombre: ['', this.soloLetras],
      cedula: ['', this.soloNumPuntos]
    });
  }

  agregarMiembroAdmon()    { this.organoAdmonArray.push(this.mkMiembroConCargo()); }
  agregarMiembroComision() { this.comisionArray.push(this.mkMiembroSimple()); }
  agregarMiembroControl()  { this.organoControlArray.push(this.mkMiembroConCargo()); }

  eliminarFila(array: FormArray, index: number): void { array.removeAt(index); }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.clubId    = Number(id);
      this.isEditing = true;
      this.cargarClub(this.clubId);
    }
  }

  cargarClub(id: number): void {
    this.loading = true;
    this.clubService.getById(id).subscribe({
      next: (club) => {
        this.form.patchValue({
          nombre:               club.nombre,
          disciplinaDeportiva:  club.disciplinaDeportiva,
          descripcionDeportiva: club.descripcionDeportiva || '',
          direccion:            club.direccion            || '',
          telefono:             club.telefono             || '',
          email:                club.email                || '',
          numeroResolucion:          club.numeroResolucion          || '',
          fechaExpedicionResolucion: club.fechaExpedicionResolucion || '',
          fechaInicioReconocimiento: club.fechaInicioReconocimiento || '',
          fechaFinReconocimiento:    club.fechaFinReconocimiento    || '',
          fechaInicioOrganoAdmon:    club.fechaInicioOrganoAdmon    || '',
          fechaFinOrganoAdmon:       club.fechaFinOrganoAdmon       || '',
          representanteLegalNombre: club.representanteLegalNombre || '',
          representanteLegalCedula: club.representanteLegalCedula || '',
          representanteLegalCargo:  club.representanteLegalCargo  || '',
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar el club', 'OK', { duration: 3000 });
      }
    });
  }

  async onSubmit(): Promise<void> {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;

    try {
      let targetId: number;
      const clubData = this.buildClubData();

      if (this.isEditing && this.clubId) {
        await firstValueFrom(this.clubService.actualizar(this.clubId, clubData));
        targetId = this.clubId;
      } else {
        const creado = await firstValueFrom(this.clubService.crear(clubData));
        targetId = creado.id!;
      }

      await this.enviarMiembros(targetId, this.organoAdmonArray,   TipoOrgano.ORGANO_ADMINISTRACION);
      await this.enviarMiembros(targetId, this.comisionArray,      TipoOrgano.COMISION_DISCIPLINARIA);
      await this.enviarMiembros(targetId, this.organoControlArray, TipoOrgano.ORGANO_CONTROL);

      this.saving = false;
      this.snackBar.open(
        this.isEditing ? 'Club actualizado correctamente' : 'Club creado correctamente',
        'OK', { duration: 4000 }
      );
      this.router.navigate(['/admin/clubes']);
    } catch (err: any) {
      this.saving = false;
      const msg = err?.error ?? err?.message ?? 'Error al guardar el club';
      this.snackBar.open('Error: ' + msg, 'OK', { duration: 8000 });
    }
  }

  private buildClubData(): Club {
    const v = this.form.value;
    return {
      nombre:               v.nombre             ?? '',
      disciplinaDeportiva:  v.disciplinaDeportiva ?? '',
      descripcionDeportiva: v.descripcionDeportiva || undefined,
      direccion:            v.direccion            || undefined,
      telefono:             v.telefono             || undefined,
      email:                v.email                || undefined,
      numeroResolucion:     v.numeroResolucion     || undefined,
      fechaExpedicionResolucion: v.fechaExpedicionResolucion || undefined,
      fechaInicioReconocimiento: v.fechaInicioReconocimiento || undefined,
      fechaFinReconocimiento:    v.fechaFinReconocimiento    || undefined,
      fechaInicioOrganoAdmon:    v.fechaInicioOrganoAdmon    || undefined,
      fechaFinOrganoAdmon:       v.fechaFinOrganoAdmon       || undefined,
      representanteLegalNombre: v.representanteLegalNombre || undefined,
      representanteLegalCedula: v.representanteLegalCedula || undefined,
      representanteLegalCargo:  v.representanteLegalCargo  || undefined,
      estado: 'NO_VIGENTE' as any
    };
  }

  private async enviarMiembros(clubId: number, array: FormArray, tipo: TipoOrgano): Promise<void> {
    for (const ctrl of array.controls) {
      const g = ctrl as FormGroup;
      const nombre = (g.get('nombre')?.value ?? '').trim();
      if (!nombre) continue;
      try {
        await firstValueFrom(this.miembroService.crear(clubId, {
          nombre,
          cedula:     g.get('cedula')?.value || '',
          cargo:      g.get('cargo')?.value  || '',
          tipoOrgano: tipo
        }));
      } catch (e) { console.warn('Error al crear miembro:', e); }
    }
  }

}
