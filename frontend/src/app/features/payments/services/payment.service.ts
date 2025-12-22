import { Injectable, inject } from '@angular/core';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';

export interface PaymentIntentResponse {
  clientSecret: string;
  paymentIntentId: string;
  amount: number;
  currency: string;
}

export interface PaymentConfigResponse {
  publishableKey: string;
  currency: string;
  defaultAmount: string;
}

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private http = inject(Http);

  getConfig() {
    return this.http.get<PaymentConfigResponse>(API_ENDPOINTS.PAYMENTS.CONFIG);
  }

  createPaymentIntent(orderId: number) {
    return this.http.post<PaymentIntentResponse>(API_ENDPOINTS.PAYMENTS.INTENTS, { orderId });
  }
}
