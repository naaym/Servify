import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { AsideComponent } from '../../../client/components/aside/aside.component';
import { ShowMessageService } from '../../../../shared/services/showmessage.service';
import { PaymentHistoryItem, PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-client-payment-history',
  standalone: true,
  imports: [CommonModule, AsideComponent],
  templateUrl: './client-payment-history.component.html',
})
export class ClientPaymentHistoryComponent implements OnInit {
  private paymentService = inject(PaymentService);
  private showmessage = inject(ShowMessageService);

  payments: PaymentHistoryItem[] = [];
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments() {
    this.loading = true;
    this.paymentService.getClientHistory().subscribe({
      next: (payments: PaymentHistoryItem[]) => {
        this.payments = payments;
        this.errorMessage = '';
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = err.message ?? 'Impossible de charger les paiements';
        this.loading = false;
        this.showmessage.show('error', this.errorMessage);
      },
    });
  }
}
