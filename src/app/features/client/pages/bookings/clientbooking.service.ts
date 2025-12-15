import { inject, Injectable } from '@angular/core';
import { BookingResponse } from '../../../booking/models/booking-response.model';
import { BookingService } from '../../../booking/services/booking.service';

@Injectable({ providedIn: 'root' })
export class ClientBookingService {
  bookingService = inject(BookingService);

  getMyBookings(status?: string) {
    return this.bookingService.getClientBookings(status);
  }

  getBookingsById(id: number) {
    return this.bookingService.getBookingById(id);
  }
}
