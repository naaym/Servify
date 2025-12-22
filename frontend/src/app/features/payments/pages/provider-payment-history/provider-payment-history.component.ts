import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { ChatNotificationService } from '../../../chat/services/chat-notification.service';
import { PaymentHistoryItem, PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-provider-payment-history',
  standalone: true,
  imports: [CommonModule, DatePipe, RouterModule],
  templateUrl: './provider-payment-history.component.html',
})
export class ProviderPaymentHistoryComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly chatNotificationService = inject(ChatNotificationService);
  private readonly paymentService = inject(PaymentService);

  readonly unreadCount$ = this.chatNotificationService.unreadCount$;
  payments: PaymentHistoryItem[] = [];
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.chatNotificationService.startPolling();
    this.loadPayments();
  }

  loadPayments() {
    this.loading = true;
    this.paymentService.getProviderHistory().subscribe({
      next: (payments: PaymentHistoryItem[]) => {
        this.payments = payments;
        this.errorMessage = '';
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = err.message ?? 'Impossible de charger les paiements';
        this.loading = false;
      },
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
