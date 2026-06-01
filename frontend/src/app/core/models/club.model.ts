export enum EstadoClub {
  VIGENTE = 'VIGENTE',
  NO_VIGENTE = 'NO_VIGENTE'
}

export enum TipoOrgano {
  ORGANO_ADMINISTRACION  = 'ORGANO_ADMINISTRACION',
  COMISION_DISCIPLINARIA = 'COMISION_DISCIPLINARIA',
  ORGANO_CONTROL         = 'ORGANO_CONTROL'
}


export interface Club {
  id?: number;
  nombre: string;
  numeroClub?: string;
  disciplinaDeportiva: string;
  descripcionDeportiva?: string;
  direccion?: string;
  telefono?: string;
  email?: string;
  numeroResolucion?: string;
  fechaExpedicionResolucion?: string;
  // Reconocimiento deportivo
  fechaInicioReconocimiento?: string;
  fechaFinReconocimiento?: string;
  // Órgano de administración
  fechaInicioOrganoAdmon?: string;
  fechaFinOrganoAdmon?: string;
  // Representante legal
  representanteLegalNombre?: string;
  representanteLegalCedula?: string;
  representanteLegalCargo?: string;
  estado: EstadoClub;
  estadoDescripcion?: string;
  imagenUrl?: string;
  documentoPdfUrl?: string;
  fechaCreacion?: string;
}

export interface MiembroOrgano {
  id?: number;
  clubId?: number;
  clubNombre?: string;
  nombre: string;
  cedula?: string;
  tipoOrgano: TipoOrgano;
  tipoOrganoDescripcion?: string;
  cargo?: string;
  fechaCreacion?: string;
}

