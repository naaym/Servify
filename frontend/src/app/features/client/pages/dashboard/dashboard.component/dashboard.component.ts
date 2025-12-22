import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { AsideComponent } from '../../../components/aside/aside.component';
import { BookingsListComponent } from '../../bookings/list/requests-list.component/bookings-list.component';
import { ClientBookingService } from '../../bookings/clientbooking.service';
import { StatsBooking } from '../../bookings/statsbooking.model';
import { StatCardComponent } from '../../../components/stat-card/stat-card.component/stat-card.component';
import { ShowMessageService } from '../../../../../shared/services/showmessage.service';
import { PaymentHistoryItem, PaymentService } from '../../../payments/services/payment.service';

@Component({
  selector: 'app-dashboard.component',
  imports: [CommonModule, BookingsListComponent, AsideComponent, StatCardComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})

export class DashboardComponent implements OnInit {
  clientbooking = inject(ClientBookingService);
  showmessage = inject(ShowMessageService);
  paymentService = inject(PaymentService);
  stats: StatsBooking | null = null;
  errorMessage = "";
  payments: PaymentHistoryItem[] = [];
  paymentsError = "";
  paymentsLoading = false;

  ngOnInit(): void {
    this.loadStats();
    this.loadPayments();
  }

  loadStats() {
    this.clientbooking.getMyStats().subscribe({
      next: (res) => {
        this.stats = res;
        this.errorMessage = "";
      },
      error: (err) => {
        this.errorMessage = err.message ?? 'Impossible de charger les statistiques';
        this.showmessage.show('error', this.errorMessage);
      },
    });
  }

  onBookingStatusChanged() {
    this.loadStats();
  }

  loadPayments() {
    this.paymentsLoading = true;
    this.paymentService.getClientHistory().subscribe({
      next: (payments) => {
        this.payments = payments;
        this.paymentsError = "";
        this.paymentsLoading = false;
      },
      error: (err) => {
        this.paymentsError = err.message ?? 'Impossible de charger les paiements';
        this.paymentsLoading = false;
        this.showmessage.show('error', this.paymentsError);
      },
    });
  }
}
