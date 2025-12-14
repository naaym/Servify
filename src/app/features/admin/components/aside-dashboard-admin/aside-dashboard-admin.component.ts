import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'aside-dashboard-admin',
  imports: [CommonModule],
  templateUrl: './aside-dashboard-admin.html',
  styleUrl: './aside-dashboard-admin.scss',
})
export class AsideDashboardAdmin {
  @Input() activeSection: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins' = 'dashboard';
  @Input() isSuperAdmin = false;
  @Output() sectionChange = new EventEmitter<'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins'>();

  navigate(section: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins') {
    if (section !== this.activeSection) {
      this.sectionChange.emit(section);
    }
  }
}
