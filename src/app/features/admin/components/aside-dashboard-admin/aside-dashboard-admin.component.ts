import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { AdminDashboardStats } from '../../models/admin-dashboard-stats.model';

@Component({
  selector: 'aside-dashboard-admin',
  imports: [CommonModule],
  templateUrl: './aside-dashboard-admin.html',
  styleUrl: './aside-dashboard-admin.scss',
})
export class AsideDashboardAdmin {
  @Input() stats?: AdminDashboardStats;
}
