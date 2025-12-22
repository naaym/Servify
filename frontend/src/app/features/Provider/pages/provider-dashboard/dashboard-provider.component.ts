import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ProviderBookingService } from '../../services/provider-booking.service';
import { ProviderBookingResponse } from '../../models/provider-booking.model';
import { Status } from '../../../booking/models/status.model';
import { AuthService } from '../../../auth/services/auth.service';
import { ChatNotificationService } from '../../../chat/services/chat-notification.service';
import { PaymentHistoryItem, PaymentService } from '../../../payments/services/payment.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, DatePipe, RouterModule],
  templateUrl: './dashboard-provider.html',
  styleUrl: './dashboard.scss',
})
export class ProviderDashboard implements OnInit {
     private readonly authService = inject(AuthService);
    private readonly router = inject(Router);
    private readonly chatNotificationService = inject(ChatNotificationService);
    readonly unreadCount$ = this.chatNotificationService.unreadCount$;
  bookings: ProviderBookingResponse[] = [];
  payments: PaymentHistoryItem[] = [];
  errorMessage = '';
  paymentsError = '';
  loading = false;
  paymentsLoading = false;

  constructor(
    private readonly bookingService: ProviderBookingService,
    private readonly paymentService: PaymentService
  ) {}

  ngOnInit(): void {
    this.chatNotificationService.startPolling();
    this.loadBookings();
    this.loadPayments();
  }

  loadBookings() {
    this.loading = true;
    this.bookingService.getProviderBookings().subscribe({
      next: (bookings) => {
        this.bookings = bookings;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      },
    });
  }

  loadPayments() {
    this.paymentsLoading = true;
    this.paymentService.getProviderHistory().subscribe({
      next: (payments: PaymentHistoryItem[]) => {
        this.payments = payments;
        this.paymentsError = '';
        this.paymentsLoading = false;
      },
      error: (err: any) => {
        this.paymentsError = err.message ?? 'Impossible de charger les paiements';
        this.paymentsLoading = false;
      },
    });
  }

  onUpdateStatus(bookingId: number, status: Status) {
    this.bookingService.updateStatus(bookingId, status).subscribe({
      next: (updated) => {
        this.bookings = this.bookings.map((booking) =>
          booking.bookingId === bookingId
            ? { ...booking, status: updated.status as Status }
            : booking
        );
      },
      error: (err) => (this.errorMessage = err.message),
    });
  }

  get pendingCount() {
    return this.bookings.filter((b) => b.status === 'PENDING').length;
  }

  get acceptedCount() {
    return this.bookings.filter((b) => b.status === 'ACCEPTED').length;
  }

  get completedCount() {
    return this.bookings.filter((b) => b.status === 'DONE').length;
  }

    logout(){
    this.authService.logout();
    this.router.navigate(['/'])
}
}
