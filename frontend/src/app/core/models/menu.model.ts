/**
 * Modelo que representa un ítem del menú de navegación del panel de administración.
 * Incluye la lista recursiva de ítems hijos, permitiendo árboles de cualquier profundidad.
 */
export interface OpcionMenuResponse {
  id: number;
  nombre: string;
  ruta?: string;
  icono?: string;
  orden: number;
  hijos: OpcionMenuResponse[];
}
