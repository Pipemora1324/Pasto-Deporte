import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { OpcionMenuResponse } from '../../../core/models/menu.model';

/**
 * Componente recursivo que renderiza un ítem del menú de navegación.
 *
 * La clave de la recursividad en Angular es que el componente se incluye
 * a sí mismo en su propio array `imports`. Cuando un ítem tiene hijos,
 * renderiza <app-menu-item> por cada hijo, que a su vez puede tener más hijos,
 * formando un árbol de profundidad arbitraria.
 *
 * Caso base: ítem sin hijos → se renderiza como enlace directo.
 * Caso recursivo: ítem con hijos → se renderiza con submenu que vuelve a
 * instanciar <app-menu-item> para cada hijo.
 */
@Component({
  selector: 'app-menu-item',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatListModule,
    MatIconModule,
    MenuItemComponent   // auto-referencia — habilita la recursión en el template
  ],
  template: `
    <!-- Caso base: ítem hoja sin hijos, renderizado como enlace directo -->
    <ng-container *ngIf="!item.hijos?.length; else conHijos">
      <a mat-list-item
         [routerLink]="item.ruta"
         routerLinkActive="active-nav-item"
         [routerLinkActiveOptions]="{ exact: item.ruta === '/admin/dashboard' }">
        <mat-icon matListItemIcon>{{ item.icono }}</mat-icon>
        <span matListItemTitle>{{ item.nombre }}</span>
      </a>
    </ng-container>

    <!-- Caso recursivo: ítem con hijos, despliega un submenu -->
    <ng-template #conHijos>
      <a mat-list-item class="parent-item">
        <mat-icon matListItemIcon>{{ item.icono }}</mat-icon>
        <span matListItemTitle>{{ item.nombre }}</span>
        <mat-icon matListItemMeta>expand_more</mat-icon>
      </a>
      <div class="submenu-hijos">
        <!-- Llamada recursiva: MenuItemComponent se renderiza a sí mismo -->
        <app-menu-item
          *ngFor="let hijo of item.hijos"
          [item]="hijo">
        </app-menu-item>
      </div>
    </ng-template>
  `,
  styles: [`
    .submenu-hijos {
      padding-left: 16px;
      border-left: 2px solid rgba(255,255,255,0.15);
      margin-left: 16px;
    }
    .parent-item {
      cursor: default;
    }
  `]
})
export class MenuItemComponent {
  /** Ítem del menú a renderizar, incluyendo su lista de hijos */
  @Input() item!: OpcionMenuResponse;
}
