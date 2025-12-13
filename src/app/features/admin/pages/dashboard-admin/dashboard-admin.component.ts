import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { AsideDashboardAdmin } from "../../components/aside-dashboard-admin/aside-dashboard-admin.component";
import { AdminService } from '../../services/admin.service';
import { ProviderApplication, ProviderStatus } from '../../models/provider-application.model';
import { AdminDashboardStats } from '../../models/admin-dashboard-stats.model';

@Component({
  selector: 'app-dashboard-admin',
  imports: [CommonModule, AsideDashboardAdmin],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.scss',
})
export class DashboardAdmin implements OnInit {
  private readonly adminService = inject(AdminService);

  dashboardStats?: AdminDashboardStats;
  providerRequests: ProviderApplication[] = [];
  publishedProviders: ProviderApplication[] = [];
  selectedStatus: ProviderStatus | undefined = 'PENDING';
  loadingRequests = false;
  loadingPublished = false;
  loadingStats = false;
  error?: string;
  selectedProvider?: ProviderApplication;

  ngOnInit() {
    this.refreshData();
  }

  refreshData() {
    this.loadDashboardStats();
    this.loadProviderRequests(this.selectedStatus);
    this.loadPublishedProviders();
  }

  loadDashboardStats() {
    this.loadingStats = true;
    this.adminService.getDashboardStats().subscribe({
      next: (stats) => {
        this.dashboardStats = stats;
        this.loadingStats = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de charger les statistiques';
        this.loadingStats = false;
      },
    });
  }

  loadProviderRequests(status?: ProviderStatus) {
    this.loadingRequests = true;
    this.selectedStatus = status;
    this.adminService.getProviderRequests(status).subscribe({
      next: (requests) => {
        this.providerRequests = requests;
        this.loadingRequests = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Une erreur est survenue';
        this.loadingRequests = false;
      },
    });
  }

  loadPublishedProviders() {
    this.loadingPublished = true;
    this.adminService.getProviderRequests('ACCEPTED').subscribe({
      next: (providers) => {
        this.publishedProviders = providers;
        this.loadingPublished = false;
      },
      error: () => {
        this.loadingPublished = false;
      },
    });
  }

  updateStatus(providerId: number, status: ProviderStatus) {
    this.loadingRequests = true;
    this.adminService.decideProvider(providerId, status).subscribe({
      next: () => {
        this.refreshData();
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de mettre à jour le statut';
        this.loadingRequests = false;
      },
    });
  }

  viewApplication(provider: ProviderApplication) {
    this.selectedProvider = provider;
  }

  closeApplication() {
    this.selectedProvider = undefined;
  }
}
