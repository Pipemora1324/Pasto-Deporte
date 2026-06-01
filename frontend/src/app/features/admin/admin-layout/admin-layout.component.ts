import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../../../core/services/auth.service';
import { ReporteService } from '../../../core/services/reporte.service';
import { MenuService } from '../../../core/services/menu.service';
import { MenuItemComponent } from '../menu-item/menu-item.component';
import { OpcionMenuResponse } from '../../../core/models/menu.model';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule,
    MatTooltipModule,
    MenuItemComponent
  ],
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.scss']
})
export class AdminLayoutComponent implements OnInit {
  authService     = inject(AuthService);
  private reporteService = inject(ReporteService);
  private menuService    = inject(MenuService);
  private router         = inject(Router);

  sidenavOpen = true;
  menuItems: OpcionMenuResponse[] = [];

  ngOnInit(): void {
    this.menuService.getMenu().subscribe(items => {
      this.menuItems = items;
    });
  }

  toggleSidenav(): void {
    this.sidenavOpen = !this.sidenavOpen;
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

  logout(): void {
    this.authService.logout();
  }

  get userInfo() {
    return this.authService.getUserInfo();
  }
}
